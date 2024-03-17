package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByCategoryId(Long categoryId);
    boolean existsByBookGroupId(Long bookGroupId);

    List<Book> findByTitle(String title);
}