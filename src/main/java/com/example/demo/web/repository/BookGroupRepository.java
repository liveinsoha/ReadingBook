package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.BookGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookGroupRepository extends JpaRepository<BookGroup, Long> {
    List<BookGroup> findByTitle(String title);
    List<BookGroup> findByTitleContaining(String title);
}