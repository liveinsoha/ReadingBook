package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {
    boolean existsByName(String name);
    Optional<CategoryGroup> findByName(String name);
}