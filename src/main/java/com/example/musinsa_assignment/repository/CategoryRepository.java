package com.example.musinsa_assignment.repository;

import com.example.musinsa_assignment.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentCategoryId(Long id);
    Optional<Category> findByName(String name);
}
