package com.example.musinsa_assignment.exception;

import org.springframework.http.HttpStatus;

public abstract class CategoryRuntimeException extends RuntimeException {

    public CategoryRuntimeException(String message) {
        super(message);
    }

    public CategoryRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract HttpStatus getStatus();
}
