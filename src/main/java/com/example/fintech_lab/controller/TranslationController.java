package com.example.fintech_lab.controller;

import com.example.fintech_lab.dto.translation.TranslationRequest;
import com.example.fintech_lab.dto.translation.TranslationResponse;
import com.example.fintech_lab.service.TranslationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TranslationController {
    private final TranslationService translationService;

    @PostMapping("/translate")
    public TranslationResponse translate(@RequestBody @Valid TranslationRequest translationRequest)
            throws ExecutionException, InterruptedException, UnknownHostException {
        return new TranslationResponse(translationService.translateText(translationRequest));
    }

}

