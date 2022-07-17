package com.example.musinsa_assignment.service;

import com.example.musinsa_assignment.domain.Category;
import com.example.musinsa_assignment.dto.CategoryDtoList;
import com.example.musinsa_assignment.dto.CategoryFindResponse;
import com.example.musinsa_assignment.exception.CategoryNotFoundException;
import com.example.musinsa_assignment.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public CategoryDtoList findCategories() {
        List<Category> findCategories = categoryRepository.findAll();
        vaildateCategroiesExists(findCategories);
        List<CategoryFindResponse> categoryFindResponses = findCategories.stream()
                .map(m -> new CategoryFindResponse(m.getId(), m.getName()))
                .collect(Collectors.toList());

        return new CategoryDtoList(categoryFindResponses);
    }

    private void vaildateCategroiesExists(List<Category> findCategories) {
        if(findCategories.isEmpty()){
            throw new CategoryNotFoundException();
        }
    }

}
