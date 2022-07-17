package com.example.musinsa_assignment.controller;

import com.example.musinsa_assignment.dto.CategoryDtoList;
import com.example.musinsa_assignment.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 전체 조회(하위 카테고리 조회는?)
     */
    @GetMapping
    public CategoryDtoList findCategories() {
        return categoryService.findCategories();
    }

    /**
     * 하위 카테고리 조회
     */
    @GetMapping("/{id}")
    public CategoryDtoList findChildCategories(@PathVariable Long id) {
        return categoryService.findChildCategories(id);
    }
}

