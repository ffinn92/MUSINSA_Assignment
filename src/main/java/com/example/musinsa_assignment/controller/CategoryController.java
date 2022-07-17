package com.example.musinsa_assignment.controller;

import com.example.musinsa_assignment.dto.CategoryCreateRequest;
import com.example.musinsa_assignment.dto.CategoryCreateResponse;
import com.example.musinsa_assignment.dto.CategoryDtoList;
import com.example.musinsa_assignment.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 카테고리 등록
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryCreateResponse createCategory(@RequestBody CategoryCreateRequest request) {
        return categoryService.createCategory(request);
    }

    /**
     * 카테고리 수정
     */
    @PatchMapping("/{id}")
    public CategoryEditResponse editCategory(@PathVariable Long id, @RequestParam(name = "name") String name) {
        return categoryService.editCategoryById(id, name);
    }
}

