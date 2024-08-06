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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TranslationServiceTest {

    @Mock
    private TranslationRepository translationRepository;

    @Mock
    private TranslatorApiController translatorApiController;

    @Mock
    private ExecutorService executorService;

    @InjectMocks
    private TranslationService translationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTranslateText_Success() throws InterruptedException, ExecutionException {
        TranslationRequest translationRequest = new TranslationRequest("en", "ru", "hello world");
        GetAllLanguagesResponse languagesResponse = new GetAllLanguagesResponse(
                List.of(new LanguageResponse("en", "English"), new LanguageResponse("ru", "Russian"))
        );
        when(translatorApiController.getLanguages(any(GetAllLanguagesRequest.class)))
                .thenReturn(languagesResponse);

        when(translatorApiController.translateWord(any(YandexRequest.class)))
                .thenReturn("привет")
                .thenReturn("мир");

        Future<String> futureMock1 = mock(Future.class);
        Future<String> futureMock2 = mock(Future.class);
        when(futureMock1.get()).thenReturn("привет");
        when(futureMock2.get()).thenReturn("мир");

        when(executorService.submit(any(Callable.class)))
                .thenReturn(futureMock1)
                .thenReturn(futureMock2);

        String ip = "172.23.128.1";
        String result = translationService.translateText(translationRequest, ip);

        ArgumentCaptor<TranslationTextEntity> captor = ArgumentCaptor.forClass(TranslationTextEntity.class);
        verify(translationRepository).save(captor.capture());
        TranslationTextEntity entity = captor.getValue();

        assertEquals(ip, entity.ipAddress());
        assertEquals("hello world", entity.inputText());
    }


    @Test
    public void testTranslateTextSourceLanguageNotFound() {
        TranslationRequest translationRequest = new TranslationRequest("fre", "ru", "bonjour");
        GetAllLanguagesResponse languagesResponse = new GetAllLanguagesResponse(
                List.of(new LanguageResponse("ru", "Russian"))
        );
        when(translatorApiController.getLanguages(any(GetAllLanguagesRequest.class)))
                .thenReturn(languagesResponse);

        String ip = "172.23.128.1";
        assertThrows(LanguageNotFoundException.class, () -> translationService.translateText(translationRequest, ip));
    }

    @Test
    public void testTranslateTextTargetLanguageNotFound() {
        TranslationRequest translationRequest = new TranslationRequest("en", "fre", "hello");
        GetAllLanguagesResponse languagesResponse = new GetAllLanguagesResponse(
                List.of(new LanguageResponse("en", "English"))
        );
        when(translatorApiController.getLanguages(any(GetAllLanguagesRequest.class)))
                .thenReturn(languagesResponse);

        String ip = "172.23.128.1";
        assertThrows(LanguageNotFoundException.class, () -> translationService.translateText(translationRequest, ip));
    }


}
