package com.example.musinsa_assignment.dto;

import com.example.musinsa_assignment.domain.Category;
import lombok.Getter;

@Getter
public class CategoryDeleteResponse {

    private Long id;

    public CategoryDeleteResponse(Long id) {
        this.id = id;
    }

    public static CategoryDeleteResponse from(Category category) {
        return new CategoryDeleteResponse(category.getId());
    }
}
