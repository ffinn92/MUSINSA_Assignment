package com.example.musinsa_assignment.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CategoryDtoList<T> {

    private final List<T> data;

    public CategoryDtoList(List<T> dtos) {
        this.data = dtos;
    }

}
