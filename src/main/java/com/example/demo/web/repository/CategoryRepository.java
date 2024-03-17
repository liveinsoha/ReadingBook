package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    boolean existsByCategoryGroupId(Long categoryGroupId);
}