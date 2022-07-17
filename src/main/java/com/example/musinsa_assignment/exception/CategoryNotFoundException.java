package com.example.musinsa_assignment.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends CategoryRuntimeException{

    public CategoryNotFoundException() {
        super("카테고리가 존재하지 않습니다.");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
