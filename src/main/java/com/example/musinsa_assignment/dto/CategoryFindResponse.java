package com.example.musinsa_assignment.dto;

import com.example.musinsa_assignment.domain.Category;
import lombok.Getter;

@Getter
public class CategoryFindResponse {

    private Long id;

    private String name;

    public CategoryFindResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryFindResponse from(Category category) {
        return new CategoryFindResponse(
                category.getId(),
                category.getName());
    }


}
