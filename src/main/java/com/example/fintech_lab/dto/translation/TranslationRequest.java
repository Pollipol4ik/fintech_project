package com.example.fintech_lab.dto.translation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TranslationRequest(@Schema(description = "Язык текущего текста", example = "en")
                                 @Size(min = 2, max = 7, message = "Код языка должен состоять минимум из 2 симвов, максимум из 7.")
                                 @NotBlank(message = "Код текущего языка не должен быть пустым.")
                                 String sourceLanguageCode,
                                 @Schema(description = "Язык для перевода", example = "ru")
                                 @Size(min = 2, max = 7, message = "Код языка  должен состоять миниум из 2 симвов, максимум из 7.")
                                 @NotBlank(message = "Код языка для перевода не должен быть пустым.")
                                 String targetLanguageCode,
                                 @Schema(description = "Текст для перевода", example = "Hello world, this is my first program.")
                                 @NotBlank(message = "Текст для перевода не должен быть пустым.")
                                 String sourceText) {
}
