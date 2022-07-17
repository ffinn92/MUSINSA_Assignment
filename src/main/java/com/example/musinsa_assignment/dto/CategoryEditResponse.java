package com.example.musinsa_assignment.dto;

import com.example.musinsa_assignment.domain.Category;
import lombok.Getter;

@Getter
public class CategoryEditResponse {

    private Long id;
    private String name;

    public CategoryEditResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryEditResponse from(Category category) {
        return new CategoryEditResponse(
                category.getId(),
                category.getName());
    }
}
