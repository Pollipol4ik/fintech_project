package com.example.fintech_lab.service;

import com.example.fintech_lab.controller.TranslatorApiController;
import com.example.fintech_lab.dto.translation.TranslationRequest;
import com.example.fintech_lab.dto.yandex.GetAllLanguagesRequest;
import com.example.fintech_lab.dto.yandex.GetAllLanguagesResponse;
import com.example.fintech_lab.dto.yandex.LanguageResponse;
import com.example.fintech_lab.dto.yandex.YandexRequest;
import com.example.fintech_lab.entity.TranslationTextEntity;
import com.example.fintech_lab.exceptions.LanguageNotFoundException;
import com.example.fintech_lab.repository.TranslationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TranslationServiceTest {

    @Mock
    private TranslationRepository translationRepository;

    @Mock
    private TranslatorApiController translatorApiController;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ExecutorService executorService;

    @InjectMocks
    private TranslationService translationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testTranslateText_Success() throws InterruptedException, ExecutionException, UnknownHostException {
//        TranslationRequest translationRequest = new TranslationRequest("en", "ru", "hello world");
//        GetAllLanguagesResponse languagesResponse = new GetAllLanguagesResponse(
//                List.of(new LanguageResponse("en", "English"), new LanguageResponse("ru", "Russian"))
//        );
//        when(translatorApiController.getLanguages(any(GetAllLanguagesRequest.class)))
//                .thenReturn(languagesResponse);
//        when(translatorApiController.translateWord(any(YandexRequest.class)))
//                .thenReturn("привет", "мир");
//
//        Future<String> futureMock = mock(Future.class);
//        when(futureMock.get()).thenReturn("привет", "мир");
//        when(executorService.submit(any(Callable.class))).thenReturn(futureMock);
//
//        when(httpServletRequest.getRemoteAddr()).thenReturn("172.23.128.1");
//
//        String result = translationService.translateText(translationRequest);
//        assertEquals("привет мир", result);
//
//        ArgumentCaptor<TranslationTextEntity> captor = ArgumentCaptor.forClass(TranslationTextEntity.class);
//        verify(translationRepository).save(captor.capture());
//        TranslationTextEntity entity = captor.getValue();
//
//        assertEquals("172.23.128.1", entity.ipAddress());
//        assertEquals("hello world", entity.inputText());
//        assertEquals("привет мир", entity.translatedText());
//    }

    @Test
    public void testTranslateText_SourceLanguageNotFound() {
        TranslationRequest translationRequest = new TranslationRequest("fr", "ru", "bonjour");
        GetAllLanguagesResponse languagesResponse = new GetAllLanguagesResponse(
                List.of(new LanguageResponse("ru", "Russian"))
        );
        when(translatorApiController.getLanguages(any(GetAllLanguagesRequest.class)))
                .thenReturn(languagesResponse);

        assertThrows(LanguageNotFoundException.class, () -> translationService.translateText(translationRequest));
    }

    @Test
    public void testTranslateText_TargetLanguageNotFound() {
        TranslationRequest translationRequest = new TranslationRequest("en", "fr", "hello");
        GetAllLanguagesResponse languagesResponse = new GetAllLanguagesResponse(
                List.of(new LanguageResponse("en", "English"))
        );
        when(translatorApiController.getLanguages(any(GetAllLanguagesRequest.class)))
                .thenReturn(languagesResponse);

        assertThrows(LanguageNotFoundException.class, () -> translationService.translateText(translationRequest));
    }

    @Test
    public void testSaveTranslationToDatabase_Success() throws UnknownHostException {
        String sourceText = "hello";
        String translatedText = "привет";
        when(httpServletRequest.getRemoteAddr()).thenReturn("172.23.128.1");

        translationService.saveTranslationToDatabase(sourceText, translatedText);

        ArgumentCaptor<TranslationTextEntity> captor = ArgumentCaptor.forClass(TranslationTextEntity.class);
        verify(translationRepository).save(captor.capture());
        TranslationTextEntity entity = captor.getValue();

        assertEquals("172.23.128.1", entity.ipAddress());
        assertEquals(sourceText, entity.inputText());
        assertEquals(translatedText, entity.translatedText());
    }
}
