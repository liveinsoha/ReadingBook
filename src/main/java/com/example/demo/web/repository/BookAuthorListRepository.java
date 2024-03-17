package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.BookAuthorList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookAuthorListRepository extends JpaRepository<BookAuthorList, Long> {

    Optional<BookAuthorList> findByBookIdAndAuthorId(Long bookId, Long authorId);
}