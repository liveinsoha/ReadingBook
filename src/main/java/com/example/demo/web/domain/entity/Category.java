package com.example.demo.web.domain.entity;

import com.example.demo.web.dto.request.CategoryUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category extends BaseEntity {

    @Id
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_group_id")
    private CategoryGroup categoryGroup;


    public static Category createCategory(String name, CategoryGroup categoryGroup) {
        Category category = new Category();
        category.name = name;
        category.categoryGroup = categoryGroup;
        return category;
    }


    public void updateCategory(CategoryUpdateRequest request) {
        this.name = request.getName();
    }
}