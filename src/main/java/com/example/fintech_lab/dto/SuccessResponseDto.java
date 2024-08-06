package com.example.fintech_lab.dto;

import jakarta.validation.constraints.NotNull;

public record SuccessResponseDto(@NotNull
                                 boolean success,
                                 String message) {
}
