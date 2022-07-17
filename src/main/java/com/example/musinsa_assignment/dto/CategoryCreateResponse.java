package com.example.musinsa_assignment.dto;

import com.example.musinsa_assignment.domain.Category;
import lombok.Getter;

@Getter
public class CategoryCreateResponse {

    private Long id;

    private String name;

    public CategoryCreateResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryCreateResponse from(Category category) {
        return new CategoryCreateResponse(
                category.getId(),
                category.getName());
    }

}
