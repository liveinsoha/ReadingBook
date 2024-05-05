package com.example.demo.web.domain.entity;

import com.example.demo.web.dto.request.CategoryGroupRegisterRequest;
import com.example.demo.web.dto.request.CategoryGroupUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CategoryGroup extends BaseEntity {
    @Id
    @Column(name = "category_group_id")
    private Long id;
    private String name;


    public static CategoryGroup createCategoryGroup(CategoryGroupRegisterRequest request) {
        CategoryGroup categoryGroup = new CategoryGroup();
        categoryGroup.name = request.getName();
        return categoryGroup;
    }

    public void updateCategoryGroup(CategoryGroupUpdateRequest request) {
        name = request.getName();
    }
}