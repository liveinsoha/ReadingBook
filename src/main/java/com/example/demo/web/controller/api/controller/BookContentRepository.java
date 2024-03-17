package com.example.demo.web.controller.api.controller;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.BookContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookContentRepository extends JpaRepository<BookContent, Book> {
    Optional<BookContent> findByBookId(Long bookId);
}