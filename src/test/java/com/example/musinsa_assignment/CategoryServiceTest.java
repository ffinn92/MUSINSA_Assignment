package com.example.musinsa_assignment;

import com.example.musinsa_assignment.domain.Category;
import com.example.musinsa_assignment.dto.CategoryCreateRequest;
import com.example.musinsa_assignment.dto.CategoryCreateResponse;
import com.example.musinsa_assignment.dto.CategoryDtoList;
import com.example.musinsa_assignment.exception.CategoryAlreadyExistException;
import com.example.musinsa_assignment.exception.CategoryNotFoundException;
import com.example.musinsa_assignment.repository.CategoryRepository;
import com.example.musinsa_assignment.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CategoryService 통합테스트")
@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Nested
    @DisplayName("카테고리 조회 및 가능여부 테스트")
    class getTest {

        @Test
        void 모든_카테고리를_조회할_수_있다() {
            //given
            Category parentCategory1 = new Category("상의");
            categoryRepository.save(parentCategory1);
            Category childCategory1 = new Category("반팔", parentCategory1);
            Category childCategory2 = new Category("긴팔", parentCategory1);
            categoryRepository.save(childCategory1);
            categoryRepository.save(childCategory2);

            //when
            CategoryDtoList categories = categoryService.findCategories();
            List categoriesList = categories.getData();

            //then
            assertThat(categoriesList.size()).isEqualTo(3);
        }

        @Test
        void 선택한_상위_카테고리의_하위_카테고리를_조회할_수_있다() {
            //given
            Category parentCategory1 = new Category("상의");
            categoryRepository.save(parentCategory1);
            Category childCategory1 = new Category("반팔", parentCategory1);
            Category childCategory2 = new Category("긴팔", parentCategory1);
            categoryRepository.save(childCategory1);
            categoryRepository.save(childCategory2);
            Long parentId1 = categoryRepository.findByName("상의").orElseThrow().getId();

            //when
            CategoryDtoList childCategories = categoryService.findChildCategories(parentId1);
            List childCategoriesData = childCategories.getData();

            //then
            assertThat(childCategoriesData.size()).isEqualTo(2);
        }

        @Test
        void 조회하려는_전체_카테고리가_없을_경우_예외를_발생시킨다() throws Exception {
            //given

            //when

            //then
            CategoryNotFoundException thrown = assertThrows(CategoryNotFoundException.class,
                    () -> categoryService.findCategories());
            assertEquals("카테고리가 존재하지 않습니다.", thrown.getMessage());
        }

        @Test
        void 조회하려는_상위_카테고리가_없을_경우_예외를_발생시킨다() throws Exception {
            //given

            //when

            //then
            CategoryNotFoundException thrown = assertThrows(CategoryNotFoundException.class,
                    () -> categoryService.findChildCategories(1L));
            assertEquals("카테고리가 존재하지 않습니다.", thrown.getMessage());
        }

        @Test
        void 조회하려는_하위_카테고리가_없을_경우_예외를_발생시킨다() throws Exception {
            //given
            Category parentCategory1 = new Category("상의");
            categoryRepository.save(parentCategory1);
            Long parentId1 = categoryRepository.findByName("상의").orElseThrow().getId();

            //when

            //then
            CategoryNotFoundException thrown = assertThrows(CategoryNotFoundException.class,
                    () -> categoryService.findChildCategories(parentId1));
            assertEquals("카테고리가 존재하지 않습니다.", thrown.getMessage());
        }

    }

    @Nested
    @DisplayName("카테고리 신규 등록/중복 테스트")
    class AddTest {

        @Test
        void 상위_카테고리를_신규_등록할_수_있다() {
            //given
            CategoryCreateRequest request1 = new CategoryCreateRequest("상의", null);
            CategoryCreateRequest request2 = new CategoryCreateRequest("하의", null);
            CategoryCreateRequest request3 = new CategoryCreateRequest("신발", null);

            //when
            CategoryCreateResponse category1 = categoryService.createCategory(request1);
            CategoryCreateResponse category2 = categoryService.createCategory(request2);
            CategoryCreateResponse category3 = categoryService.createCategory(request3);

            //then
            assertThat(categoryRepository.findByName(category1.getName()).orElseThrow().getName()).isEqualTo(request1.getName());
            assertThat(categoryRepository.findByName(category2.getName()).orElseThrow().getName()).isEqualTo(request2.getName());
            assertThat(categoryRepository.findByName(category3.getName()).orElseThrow().getName()).isEqualTo(request3.getName());
        }

        @Test
        void 하위_카테고리를_신규_등록할_수_있다() {
            //given
            Category parentCategory1 = new Category("상의");
            Category parentCategory2 = new Category("하의");
            categoryRepository.save(parentCategory1);
            categoryRepository.save(parentCategory2);

            Long parentId1 = categoryRepository.findByName("상의").orElseThrow().getId();
            Long parentId2 = categoryRepository.findByName("하의").orElseThrow().getId();
            CategoryCreateRequest request1 = new CategoryCreateRequest("반팔", parentId1);
            CategoryCreateRequest request2 = new CategoryCreateRequest("청바지", parentId2);

            //when
            CategoryCreateResponse category1 = categoryService.createCategory(request1);
            CategoryCreateResponse category2 = categoryService.createCategory(request2);

            //then
            assertThat(categoryRepository.findByName(category1.getName()).orElseThrow().getName()).isEqualTo(request1.getName());
            assertThat(categoryRepository.findByName(category2.getName()).orElseThrow().getName()).isEqualTo(request2.getName());
        }

        @Test
        void 중복된_이름으로_상위_카테고리를_등록시_예외를_발생시킨다() throws Exception {
            //given
            CategoryCreateRequest request1 = new CategoryCreateRequest("상의", null);
            CategoryCreateRequest request2 = new CategoryCreateRequest("상의", null);

            //when
            categoryService.createCategory(request1);

            //then
            CategoryAlreadyExistException thrown = assertThrows(CategoryAlreadyExistException.class, () -> categoryService.createCategory(request2));
            assertEquals("동일한 카테고리명이 존재합니다.", thrown.getMessage());
        }

        @Test
        void 중복된_이름으로_하위_카테고리를_등록시_예외를_발생시킨다() throws Exception {
            //given
            Category parentCategory1 = new Category("상의");
            categoryRepository.save(parentCategory1);

            Long parentId1 = categoryRepository.findByName("상의").orElseThrow().getId();
            Long parentId2 = categoryRepository.findByName("상의").orElseThrow().getId();
            CategoryCreateRequest request1 = new CategoryCreateRequest("반팔", parentId1);
            CategoryCreateRequest request2 = new CategoryCreateRequest("반팔", parentId2);

            //when
            categoryService.createCategory(request1);

            //then
            CategoryAlreadyExistException thrown = assertThrows(CategoryAlreadyExistException.class, () -> categoryService.createCategory(request2));
            assertEquals("동일한 카테고리명이 존재합니다.", thrown.getMessage());
        }
    }

    @Nested
    @DisplayName("카테고리 수정 및 가능여부 테스트")
    class editTest {
        @Test
        void 요청받은_카테고리의_이름을_수정할_수_있다() {
            //given
            Category parentCategory1 = new Category("상의");
            categoryRepository.save(parentCategory1);
            Long parentId1 = categoryRepository.findByName("상의").orElseThrow().getId();

            //when
            categoryService.editCategoryById(parentId1, "하의");

            //then
            assertThat(categoryRepository.findById(parentId1).orElseThrow().getName()).isEqualTo("하의");
        }

        @Test
        void 수정하려는_카테고리가_없을_경우_예외를_발생시킨다() throws Exception {
            //given

            //when

            //then
            CategoryNotFoundException thrown = assertThrows(CategoryNotFoundException.class,
                    () -> categoryService.editCategoryById(1L, "의류"));
            assertEquals("카테고리가 존재하지 않습니다.", thrown.getMessage());
        }
    }

    @Nested
    @DisplayName("카테고리 삭제 및 가능여부 테스트")
    class deleteTest {
        @Test
        void 요청받은_상위_카테고리를_삭제할_수_있다() {
            //given
            Category parentCategory1 = new Category("상의");
            categoryRepository.save(parentCategory1);
            Category childCategory1 = new Category("반팔", parentCategory1);
            categoryRepository.save(childCategory1);
            Long parentId1 = categoryRepository.findByName("상의").orElseThrow().getId();

            //when
            categoryService.deleteCatgoryById(parentId1);

            //then
            assertThat(categoryRepository.findByName("상의").isPresent()).isEqualTo(false);
        }

        @Test
        void 요청받은_하위_카테고리를_삭제할_수_있다() {
            //given
            Category parentCategory1 = new Category("상의");
            categoryRepository.save(parentCategory1);
            Category childCategory1 = new Category("반팔", parentCategory1);
            categoryRepository.save(childCategory1);
            Long childId1 = categoryRepository.findByName("반팔").orElseThrow().getId();

            //when
            categoryService.deleteCatgoryById(childId1);

            //then
            assertThat(categoryRepository.findByName("반팔").isPresent()).isEqualTo(false);
        }

        @Test
        void 삭제하려는_카테고리가_없을_경우_예외를_발생시킨다() throws Exception {
            //given

            //when

            //then
            CategoryNotFoundException thrown = assertThrows(CategoryNotFoundException.class,
                    () -> categoryService.deleteCatgoryById(1L));
            assertEquals("카테고리가 존재하지 않습니다.", thrown.getMessage());
        }
    }
}
