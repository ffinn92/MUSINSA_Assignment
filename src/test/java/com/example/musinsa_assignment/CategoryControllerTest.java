package com.example.musinsa_assignment;

import com.example.musinsa_assignment.controller.CategoryController;
import com.example.musinsa_assignment.domain.Category;
import com.example.musinsa_assignment.dto.*;
import com.example.musinsa_assignment.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CategoryController 클래스")
@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc()
class CategoryControllerTest {

    private static final String BASE_URL = "/api/categories";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Nested
    @DisplayName("카테고리 컨트롤러 단위 테스트")
    class ApiTest {

        @Test
        void 카테고리가_존재할때_전체_카테고리_조회는_성공한다() throws Exception {
            //given
            Category category = new Category("상의");
            CategoryFindResponse categoryFindResponse = new CategoryFindResponse(category.getId(), category.getName());
            List<CategoryFindResponse> categoryFindResponses = new ArrayList<>();
            categoryFindResponses.add(categoryFindResponse);
            CategoryDtoList<List<CategoryFindResponse>> categoryDtoList = new CategoryDtoList(categoryFindResponses);

            //when
            given(categoryService.findCategories()).willReturn(categoryDtoList);

            //then
            mockMvc.perform(get(BASE_URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].name").value("상의"));
        }

        @Test
        void 상위_카테고리가_존재할때_하위_카테고리_조회는_성공한다() throws Exception {
            //given
            Long parentId = 1L;
            Long id = 2L;
            CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest("반팔", parentId);
            categoryService.createCategory(categoryCreateRequest);
            CategoryFindResponse categoryFindResponse = new CategoryFindResponse(id, "반팔");
            List<CategoryFindResponse> categoryFindResponses = new ArrayList<>();
            categoryFindResponses.add(categoryFindResponse);
            CategoryDtoList<List<CategoryFindResponse>> categoryDtoList = new CategoryDtoList(categoryFindResponses);

            //when
            String findChildCategoriesUrl = BASE_URL + "/" + parentId;
            given(categoryService.findChildCategories(parentId)).willReturn(categoryDtoList);

            //then
            mockMvc.perform(get(findChildCategoriesUrl))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].name").value("반팔"));
        }

        @Test
        void 동일한_카테고리가_존재하지_않을때_카테고리_생성은_성공한다() throws Exception {
            //given
            Long parentId = null;
            CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest("상의", parentId);
            ObjectMapper mapper = new ObjectMapper();
            String requestBdoy = mapper.writeValueAsString(categoryCreateRequest);
            CategoryCreateResponse categoryCreateResponse = new CategoryCreateResponse(1L, "상의");

            //when
            given(categoryService.createCategory(any(CategoryCreateRequest.class))).willReturn(categoryCreateResponse);
            ResultActions performResult = mockMvc.perform(post(BASE_URL)
                    .content(requestBdoy)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            performResult.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("상의"));
        }

        @Test
        void 요청받은_카테고리가_존재할때_카테고리_수정은_성공한다() throws Exception {
            //given
            Long id = 1L;
            Long parentId = null;
            String editedName = "하의";
            createdInitalCategory(parentId);
            CategoryEditResponse categoryEditResponse = new CategoryEditResponse(id, editedName);

            //when
            given(categoryService.editCategoryById(id, editedName)).willReturn(categoryEditResponse);
            String editCategoryUrl = BASE_URL + "/" + id + "?name=" + editedName;

            //then
            ResultActions performResult = mockMvc.perform(patch(editCategoryUrl))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("하의"));
        }

        @Test
        void 요청받은_카테고리가_존재할때_카테고리_삭제_성공한다() throws Exception {
            //given
            Long id = 1L;
            Long parentId = null;
            createdInitalCategory(parentId);
            CategoryDeleteResponse categoryDeleteResponse = new CategoryDeleteResponse(id);

            //when
            given(categoryService.deleteCatgoryById(id)).willReturn(categoryDeleteResponse);
            String deleteCategoryUrl = BASE_URL + "/" + id;

            //then
            ResultActions performResult = mockMvc.perform(delete(deleteCategoryUrl))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L));
        }

        private void createdInitalCategory(Long parentId) {
            CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest("상의", parentId);
            categoryService.createCategory(categoryCreateRequest);
        }
    }
}