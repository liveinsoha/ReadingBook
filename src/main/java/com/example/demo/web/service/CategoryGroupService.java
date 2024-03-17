package com.example.demo.web.service;

import com.example.demo.web.domain.entity.CategoryGroup;
import com.example.demo.web.dto.request.CategoryGroupRegisterRequest;
import com.example.demo.web.dto.request.CategoryGroupUpdateRequest;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.CategoryGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryGroupService {
    private final CategoryGroupRepository categoryGroupRepository;

    /**
     * 카테고리 그룹 등록 메소드
     * @param request (등록 DTO)
     * @return categoryGroupId
     */
    public Long register(CategoryGroupRegisterRequest request) {
        validateRegisterForm(request);
        CategoryGroup categoryGroup = CategoryGroup.createCategoryGroup(request);
        return categoryGroupRepository.save(categoryGroup).getId();
    }

    public CategoryGroup findCategoryGroupById(Long categoryGroupId) {
        CategoryGroup categoryGroup = categoryGroupRepository.findById(categoryGroupId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CATEGORY_GROUP_NOT_FOUND));
        return categoryGroup;
    }

    private void validateRegisterForm(CategoryGroupRegisterRequest request) {
        String name = request.getName();

        validateNameExist(name);
        validateName(name);
    }

    private void validateNameExist(String name) {
        boolean isExist = categoryGroupRepository.existsByName(name);
        if(isExist == true){
            throw new BaseException(BaseResponseCode.DUPLICATE_CATEGORY_NAME);
        }
    }

    private void validateName(String name) {
        if(name == null || name.trim().equals("")){
            throw new IllegalArgumentException("카테고리 그룹을 입력하세요.");
        }
    }

    /**
     * 카테고리 그룹 수정 메소드
     * @param request (업데이트 DTO)
     * @param categoryGroupId
     */
    public void update(CategoryGroupUpdateRequest request, Long categoryGroupId) {
        validateUpdateForm(request);

        CategoryGroup categoryGroup = findCategoryGroupById(categoryGroupId);
        categoryGroup.updateCategoryGroup(request);
    }

    private void validateUpdateForm(CategoryGroupUpdateRequest request) {
        String name = request.getName();
        validateNameExist(name);
        validateName(name);
    }

    public void deleteCategoryGroup(Long categoryGroupId) {
    }
}