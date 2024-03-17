package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Category;
import com.example.demo.web.domain.entity.CategoryGroup;
import com.example.demo.web.dto.request.CategoryGroupRegisterRequest;
import com.example.demo.web.dto.request.CategoryGroupUpdateRequest;
import com.example.demo.web.dto.request.CategoryRegisterRequest;
import com.example.demo.web.dto.request.CategoryUpdateRequest;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.CategoryGroupRepository;
import com.example.demo.web.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryService {

    private final CategoryGroupService categoryGroupService;
    private final CategoryRepository categoryRepository;


    /**
     * 카테고리 등록 메소드
     *
     * @param request (등록 DTO)
     * @return categoryId
     */
    public Long register(CategoryRegisterRequest request) {
        Long categoryGroupId = request.getCategoryGroupId();
        String name = request.getName();

        validateRegisterForm(request);

        CategoryGroup categoryGroup = categoryGroupService.findCategoryGroupById(categoryGroupId);

        Category category = Category.createCategory(name, categoryGroup);
        return categoryRepository.save(category).getId();
    }


    public Category findCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CATEGORY_NOT_FOUND));
        return category;
    }

    private void validateRegisterForm(CategoryRegisterRequest request) {
        String name = request.getName();
        validateNameExist(name);
        validateName(name);

        Long categoryGroupId = request.getCategoryGroupId();
        if (categoryGroupId == null) {
            throw new IllegalArgumentException("카테고리 그룹 아이디를 입력하세요.");
        }
    }

    private void validateNameExist(String name) {
        boolean isExist = categoryRepository.existsByName(name);
        if (isExist) {
            throw new BaseException(BaseResponseCode.DUPLICATE_CATEGORY_NAME);
        }
    }


    private static void validateName(String name) {
        if (name == null || name.trim().equals("")) {
            throw new IllegalArgumentException("카테고리를 입력하세요.");
        }
    }

    private void validateCategoryGroupId(Long categoryGroupId) {
        if (categoryGroupId == null) {
            throw new IllegalArgumentException("카테고리 그룹 아이디를 입력하세요.");
        }
    }

    public void update(CategoryUpdateRequest request, Long categoryId) {
        validateUpdateForm(request);
        validateCategoryId(categoryId);

        Category category = findCategory(categoryId);

        category.updateCategory(request);
    }

    private void validateCategoryId(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("카테고리 아이디를 입력하세요.");
        }
    }

    private void validateUpdateForm(CategoryUpdateRequest request) {
        String name = request.getName();
        validateNameExist(name);
        validateName(name);
    }


}