package com.example.musinsa_assignment;

import com.example.musinsa_assignment.domain.Category;
import com.example.musinsa_assignment.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    void 찾으려는_카테고리의_이름으로_조회하면_카테고리가_반환된다() {
        //given
        String name = "상의";
        categoryRepository.save(new Category("상의"));

        //when
        Category category = categoryRepository.findByName("상의").orElseThrow();

        //then
        assertThat(category.getName()).isEqualTo("상의");
    }

    @Test
    void 찾으려는_하위_카테고리의_상위_카테고리_id로_조회하면_하위_카테고리가_반환된다() {
        //given
        String name = "상의";
        Category parentCategory = new Category("상의");
        categoryRepository.save(parentCategory);
        categoryRepository.save(new Category("반팔", parentCategory));
        categoryRepository.save(new Category("긴팔", parentCategory));

        Long parentId = categoryRepository.findByName("상의").orElseThrow().getId();

        //when
        List<Category> childCategories = categoryRepository.findByParentCategoryId(parentId);

        //then
        assertThat(childCategories.size()).isEqualTo(2);
        assertThat(childCategories.get(0).getName()).isEqualTo("반팔");
        assertThat(childCategories.get(1).getName()).isEqualTo("긴팔");
    }

}
