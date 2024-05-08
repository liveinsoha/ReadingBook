package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.BookContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookContentRepository extends JpaRepository<BookContent, Long> {
    Optional<BookContent> findByBookId(Long bookId);

    List<BookContent> findByBookIn(List<Book> books);

    boolean existsByBookId(Long bookId);


    BookContent findByBook(Book book);


}