package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {
}