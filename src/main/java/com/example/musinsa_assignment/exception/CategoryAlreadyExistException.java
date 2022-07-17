package com.example.musinsa_assignment.exception;

import org.springframework.http.HttpStatus;

public class CategoryAlreadyExistException extends CategoryRuntimeException{

    public CategoryAlreadyExistException() {
        super("동일한 카테고리명이 존재합니다.");
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
