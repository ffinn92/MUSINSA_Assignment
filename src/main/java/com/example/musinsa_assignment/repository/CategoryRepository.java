package com.example.musinsa_assignment.repository;

import com.example.musinsa_assignment.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentCategoryId(Long id);
}
