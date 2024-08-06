package com.example.fintech_lab.service;

import com.example.fintech_lab.controller.TranslatorApiController;
import com.example.fintech_lab.dto.translation.TranslationRequest;
import com.example.fintech_lab.dto.yandex.GetAllLanguagesRequest;
import com.example.fintech_lab.dto.yandex.YandexRequest;
import com.example.fintech_lab.entity.TranslationTextEntity;
import com.example.fintech_lab.exceptions.LanguageNotFoundException;
import com.example.fintech_lab.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class TranslationService {
    @Value("${client.yandex.folder-id}")
    private String folderId;
    private final HashSet<String> languages = new HashSet<>();

    private final TranslationRepository translationRepository;
    private final TranslatorApiController translatorApiController;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public String translateText(TranslationRequest translationRequest) throws InterruptedException, ExecutionException, UnknownHostException {
        if (languages.isEmpty()) {
            getLanguages();
        }
        if (!languages.contains(translationRequest.sourceLanguageCode())) {
            throw new LanguageNotFoundException("Не найден язык исходного сообщения");
        }
        if (!languages.contains(translationRequest.targetLanguageCode())) {
            throw new LanguageNotFoundException("Не найден язык целевого сообщения");
        }

        String[] listWords = translationRequest.sourceText().split(" ");
        List<Future<String>> futures = new ArrayList<>();
        for (String text : listWords) {
            futures.add(executorService.submit(() -> {
                YandexRequest request = new YandexRequest(folderId, List.of(text), translationRequest.targetLanguageCode(), translationRequest.sourceLanguageCode());
                return translatorApiController.translateWord(request);
            }));
        }

        StringBuilder translatedText = new StringBuilder();
        for (Future<String> future : futures) {
            translatedText.append(future.get()).append(" ");
        }

        String result = translatedText.toString().trim();
        saveTranslationToDatabase(translationRequest.sourceText(), result);

        return result;
    }

//    @Transactional
//    public void saveTranslationToDatabase(String sourceText, String translatedText) throws UnknownHostException {
////        WebAuthenticationDetails details = (WebAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
////        String ip = details.getRemoteAddress();
//        TranslationTextEntity entity = new TranslationTextEntity(InetAddress.getLocalHost().getHostAddress(), sourceText, translatedText);
//        translationRepository.save(entity);
//    }

    @Transactional
    public void saveTranslationToDatabase(String sourceText, String translatedText) throws UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        TranslationTextEntity entity = new TranslationTextEntity(ip, sourceText, translatedText);
        translationRepository.save(entity);
    }

    private void getLanguages() {
        var getAllLanguagesResponse = translatorApiController.getLanguages(new GetAllLanguagesRequest(folderId));
        for (var language : getAllLanguagesResponse.languages()) {
            languages.add(language.code());
        }
    }

}
