package com.example.musinsa_assignment.service;

import com.example.musinsa_assignment.domain.Category;
import com.example.musinsa_assignment.dto.*;
import com.example.musinsa_assignment.exception.CategoryAlreadyExistException;
import com.example.musinsa_assignment.exception.CategoryNotFoundException;
import com.example.musinsa_assignment.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    @Transactional(readOnly = true)
    public CategoryDtoList findChildCategories(Long parentId) {
        vaildateCategoryExistsById(parentId);
        List<Category> childCategories = categoryRepository.findByParentCategoryId(parentId);
        vaildateCategroiesExists(childCategories);
        List<CategoryFindResponse> categoryFindResponses = childCategories.stream()
                .map(m -> new CategoryFindResponse(m.getId(), m.getName()))
                .collect(Collectors.toList());

        return new CategoryDtoList(categoryFindResponses);
    }

    @Transactional
    public CategoryCreateResponse createCategory(CategoryCreateRequest request) {
        vaildateCategoryExistsByName(request.getName());
        if (request.getParentId() == null) { //상위 카테고리 등록
            Category saved = categoryRepository.save((new Category(request.getName())));
            return CategoryCreateResponse.from(saved);
        }
        //하위 카테고리 등록
        Category parentCategory = categoryRepository.findById(request.getParentId()).orElseThrow();
        Category saved = categoryRepository.save((new Category(request.getName(), parentCategory)));
        return CategoryCreateResponse.from(saved);
    }

    @Transactional
    public CategoryEditResponse editCategoryById(Long id, String name) {
        vaildateCategoryExistsById(id);
        Category findCategory = categoryRepository.findById(id).orElseThrow();
        findCategory.editName(name);
        return CategoryEditResponse.from(findCategory);
    }

    private void vaildateCategroiesExists(List<Category> findCategories) {
        if(findCategories.isEmpty()){
            throw new CategoryNotFoundException();
        }
    }

    private void vaildateCategoryExistsById(Long id) {
        Optional<Category> findCategory = categoryRepository.findById(id);
        if(!findCategory.isPresent()) {
            throw new CategoryNotFoundException();
        }
    }

    private void vaildateCategoryExistsByName(String name) {
        Optional<Category> findCategory = categoryRepository.findByName(name);
        if (findCategory.isPresent()) {
            throw new CategoryAlreadyExistException();
        }
    }

}
