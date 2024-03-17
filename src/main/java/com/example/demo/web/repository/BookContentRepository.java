package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.BookContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookContentRepository extends JpaRepository<BookContent, Long> {
    Optional<BookContent> findByBookId(Long bookId);

    boolean existsByBookId(Long bookId);
}