package com.example.fintech_lab.controller;

import com.example.fintech_lab.dto.translation.TranslationRequest;
import com.example.fintech_lab.service.TranslationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TranslationController.class)
public class TranslationControllerTest {

    @MockBean
    private TranslationService translationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Translate text - Success")
    public void translateTextSuccess() throws Exception {
        TranslationRequest request = new TranslationRequest("en", "ru", "Hello world");
        String translatedText = "Привет, мир";

        Mockito.when(translationService.translateText(request)).thenReturn(translatedText);

        mockMvc.perform(post("/translate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.translatedText", is(translatedText)));
    }

//    @Test
//    @DisplayName("Translate text - ExecutionException")
//    public void translateTextExecutionException() throws Exception {
//        TranslationRequest request = new TranslationRequest("en", "ru", "Hello world");
//
//        Mockito.when(translationService.translateText(request))
//                .thenThrow(new ExecutionException("Execution exception", new Throwable()));
//
//        mockMvc.perform(post("/translate")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @DisplayName("Translate text - InterruptedException")
//    public void translateTextInterruptedException() throws Exception {
//        TranslationRequest request = new TranslationRequest("en", "ru", "Hello world");
//
//        Mockito.when(translationService.translateText(request))
//                .thenThrow(new InterruptedException("Interrupted exception"));
//
//        mockMvc.perform(post("/translate")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    @DisplayName("Translate text - UnknownHostException")
//    public void translateTextUnknownHostException() throws Exception {
//        TranslationRequest request = new TranslationRequest("en", "ru", "Hello world");
//
//        Mockito.when(translationService.translateText(request))
//                .thenThrow(new UnknownHostException("Unknown host exception"));
//
//        mockMvc.perform(post("/translate")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isInternalServerError());
//    }

    @Test
    @DisplayName("Translate text - Empty Source Language Code")
    public void translateTextEmptySourceLanguageCode() throws Exception {
        TranslationRequest request = new TranslationRequest("", "ru", "Hello world");

        mockMvc.perform(post("/translate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Translate text - Source Language Code Too Short")
    public void translateTextSourceLanguageCodeTooShort() throws Exception {
        TranslationRequest request = new TranslationRequest("e", "ru", "Hello world");

        mockMvc.perform(post("/translate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Translate text - Source Language Code Too Long")
    public void translateTextSourceLanguageCodeTooLong() throws Exception {
        TranslationRequest request = new TranslationRequest("englishes", "ru", "Hello world");

        mockMvc.perform(post("/translate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Translate text - Empty Target Language Code")
    public void translateTextEmptyTargetLanguageCode() throws Exception {
        TranslationRequest request = new TranslationRequest("en", "", "Hello world");

        mockMvc.perform(post("/translate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Translate text - Target Language Code Too Short")
    public void translateTextTargetLanguageCodeTooShort() throws Exception {
        TranslationRequest request = new TranslationRequest("en", "r", "Hello world");

        mockMvc.perform(post("/translate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Translate text - Target Language Code Too Long")
    public void translateTextTargetLanguageCodeTooLong() throws Exception {
        TranslationRequest request = new TranslationRequest("en", "russianes", "Hello world");

        mockMvc.perform(post("/translate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Translate text - Empty Source Text")
    public void translateTextEmptySourceText() throws Exception {
        TranslationRequest request = new TranslationRequest("en", "ru", "");

        mockMvc.perform(post("/translate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
