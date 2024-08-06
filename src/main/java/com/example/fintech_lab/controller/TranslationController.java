package com.example.fintech_lab.controller;

import com.example.fintech_lab.dto.translation.TranslationRequest;
import com.example.fintech_lab.dto.translation.TranslationResponse;
import com.example.fintech_lab.service.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TranslationController {
    private final TranslationService translationService;

    @PostMapping("/translate")
    public TranslationResponse translate(@RequestBody
                                         @Valid
                                         TranslationRequest translationRequest,
                                         HttpServletRequest request) {
        return new TranslationResponse(translationService.translateText(translationRequest, request.getRemoteAddr()));
    }
}

