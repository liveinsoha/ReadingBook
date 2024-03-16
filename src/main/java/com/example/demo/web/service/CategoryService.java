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
    private final CategoryGroupRepository categoryGroupRepository;
    private final CategoryRepository categoryRepository;

    public Long registerCategoryGroup(CategoryGroupRegisterRequest request) {
        validateCategoryGroupRegisterForm(request);
        CategoryGroup categoryGroup = CategoryGroup.createCategoryGroup(request);
        return categoryGroupRepository.save(categoryGroup).getId();
    }

    public Long registerCategory(CategoryRegisterRequest request){
        Long categoryGroupId = request.getCategoryGroupId();
        String name = request.getName();

        validateCategoryRegisterForm(request);

        CategoryGroup categoryGroup = findCategoryGroupById(categoryGroupId);

        Category category = Category.createCategory(name, categoryGroup);
        return categoryRepository.save(category).getId();
    }

    public CategoryGroup findCategoryGroupById(Long categoryGroupId) {
        CategoryGroup categoryGroup = categoryGroupRepository.findById(categoryGroupId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CATEGORY_GROUP_NOT_FOUND));
        return categoryGroup;


    }
    public Category findCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->new BaseException(BaseResponseCode.CATEGORY_NOT_FOUND));
        return category;
    }

    private void validateCategoryRegisterForm(CategoryRegisterRequest request) {
        String name = request.getName();
        validateName(name, "카테고리를 입력하세요.");

        Long categoryGroupId = request.getCategoryGroupId();
        if(categoryGroupId == null){
            throw new IllegalArgumentException("카테고리 그룹 아이디를 입력하세요.");
        }
    }

    private void validateCategoryGroupRegisterForm(CategoryGroupRegisterRequest request) {
        String name = request.getName();
        validateName(name, "카테고리 그룹을 입력하세요.");
    }

    private static void validateName(String name, String exceptionMessage) {
        if(name == null || name.trim().equals("")){
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    public void updateCategoryGroup(CategoryGroupUpdateRequest request, Long categoryGroupId) {
        validateCategoryGroupUpdateForm(request);

        CategoryGroup categoryGroup = findCategoryGroupById(categoryGroupId);
        categoryGroup.updateCategoryGroup(request);
    }

    public void updateCategory(CategoryUpdateRequest request, Long categoryId){
        validateCategoryUpdateForm(request);

        Category category = findCategoryById(categoryId);

        Long categoryGroupId = request.getCategoryGroupId();

        if(categoryGroupId == null){
            category.updateCategory(request);
        }else {
            CategoryGroup categoryGroup = findCategoryGroupById(categoryGroupId);
            category.updateCategory(request, categoryGroup);
        }
    }

    private void validateCategoryGroupUpdateForm(CategoryGroupUpdateRequest request) {
        String name = request.getName();
        validateName(name, "카테고리 그룹을 입력하세요.");
    }

    private void validateCategoryUpdateForm(CategoryUpdateRequest request) {
        String name = request.getName();
        validateName(name, "카테고리를 입력하세요.");
    }
}