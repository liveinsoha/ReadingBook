package com.example.demo.web.service;

import com.example.demo.web.domain.entity.CategoryGroup;
import com.example.demo.web.dto.request.CategoryGroupRegisterRequest;
import com.example.demo.web.dto.request.CategoryGroupUpdateRequest;
import com.example.demo.web.dto.request.CategoryRegisterRequest;
import com.example.demo.web.exception.BaseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryGroupServiceTest {
    @Autowired
    private CategoryGroupService categoryGroupService;


    @Autowired
    private CategoryService categoryService;


    @Test
    void whenRegisteringNameNullOrBlank_thenThrowException(){
        CategoryGroupRegisterRequest nullRequest = createCategoryGroupRequest(null);
        CategoryGroupRegisterRequest blankRequest = new CategoryGroupRegisterRequest("");

        assertThatThrownBy(()->categoryGroupService.register(blankRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리 그룹을 입력하세요.");
        assertThatThrownBy(()->categoryGroupService.register(nullRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리 그룹을 입력하세요.");
    }

    @Test
    void whenCategoryGroupRegistered_thenVerifyFields(){
        //given
        CategoryGroupRegisterRequest request = createCategoryGroupRequest("소설");

        //when
        Long categoryGroupId = categoryGroupService.register(request);
        CategoryGroup categoryGroup = categoryGroupService.findCategoryGroupById(categoryGroupId);

        //then
        assertThat(categoryGroup.getId()).isEqualTo(categoryGroupId);
        assertThat(categoryGroup.getName()).isEqualTo("소설");
    }

    @Test
    void whenUpdatingNameNullOrBlank_thenThrowException(){
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        CategoryGroupUpdateRequest nullRequest = new CategoryGroupUpdateRequest(null);
        CategoryGroupUpdateRequest blankRequest = new CategoryGroupUpdateRequest("");

        assertThatThrownBy(() -> categoryGroupService.update(nullRequest, categoryGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리 그룹을 입력하세요.");
        assertThatThrownBy(() -> categoryGroupService.update(blankRequest, categoryGroupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("카테고리 그룹을 입력하세요.");
    }

    @Test
    void whenUpdatingCategoryGroupIdNotFound_thenThrowException(){
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        Long notFoundId = categoryGroupId + 1L;

        CategoryGroupUpdateRequest request = new CategoryGroupUpdateRequest("경제");

        assertThatThrownBy(() -> categoryGroupService.update(request, notFoundId))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("검색되는 카테고리 그룹이 없습니다. 카테고리 그룹 아이디를 다시 확인해주세요.");
    }

    @Test
    void whenCategoryGroupUpdated_thenVerifyFields(){
        //given
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        //when
        CategoryGroupUpdateRequest request = new CategoryGroupUpdateRequest("경제");
        categoryGroupService.update(request, categoryGroupId);

        //then
        CategoryGroup categoryGroup = categoryGroupService.findCategoryGroupById(categoryGroupId);
        assertThat(categoryGroup.getName()).isEqualTo("경제");
    }

    @Test
    void whenDeletingCategoryGroupHasCategory_thenThrowException(){
        //given
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest); //카테고리 그룹 먼저 추가

        CategoryRegisterRequest categoryRequest = createCategoryRequest("판타지 소설", categoryGroupId);
        categoryService.register(categoryRequest); //카테고리 그룹 하위에 카테고리 추가

        //하위 목록 존재할 경우 카테고리 그룹 삭제시 예외 발생
        assertThatThrownBy(() -> categoryGroupService.delete(categoryGroupId))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("해당 카테고리 그룹 아래 하위 카테고리들이 존재합니다. 하위 카테고리를 모두 삭제한 다음에 카테고리 그룹을 삭제해주세요.");
    }

    @Test
    void whenCategoryGroupDeleted_thenVerifyBoolean(){
        //given
        CategoryGroupRegisterRequest categoryGroupRequest = createCategoryGroupRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        //when //하위 목록이 없으면 삭제 가능
        boolean isDeleted = categoryGroupService.delete(categoryGroupId);

        //then
        assertThat(isDeleted).isTrue();
    }

    private CategoryRegisterRequest createCategoryRequest(String name, Long categoryGroupId) {
        return new CategoryRegisterRequest(name, categoryGroupId);
    }


    private static CategoryGroupRegisterRequest createCategoryGroupRequest(String name) {
        return new CategoryGroupRegisterRequest(name);
    }
}