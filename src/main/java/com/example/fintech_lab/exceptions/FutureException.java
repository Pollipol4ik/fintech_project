package com.example.fintech_lab.exceptions;

import org.springframework.http.HttpStatus;

public class FutureException extends BaseException {
    public FutureException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
