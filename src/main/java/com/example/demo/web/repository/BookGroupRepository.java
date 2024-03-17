package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.BookGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookGroupRepository extends JpaRepository<BookGroup, Long> {
}