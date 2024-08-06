package com.example.fintech_lab.exceptions;

import org.springframework.http.HttpStatus;

public class AccessResourceException extends BaseException {
    public AccessResourceException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
