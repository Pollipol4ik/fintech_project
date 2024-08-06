package com.example.fintech_lab.exceptions;

import org.springframework.http.HttpStatus;

public class LanguageNotFoundException extends BaseException {
    public LanguageNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
