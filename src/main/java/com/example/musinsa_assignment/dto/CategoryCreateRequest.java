package com.example.musinsa_assignment.dto;

import lombok.Getter;


@Getter
public class CategoryCreateRequest {

    private String name;
    private Long parentId;

    protected CategoryCreateRequest() {
    }

    public CategoryCreateRequest(String name, Long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

}
