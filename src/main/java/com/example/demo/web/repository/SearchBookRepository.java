package com.example.demo.web.repository;

import com.example.demo.web.dto.response.BookManageSearchResponse;
import com.example.demo.web.dto.response.BookSearchResponse;
import com.example.demo.web.service.search.BookSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchBookRepository {
    Page<BookSearchResponse> search(String query, Pageable pageable, BookSearchCondition condition);

    Page<BookManageSearchResponse> searchRegisteredBook(String query, Pageable pageable, BookSearchCondition condition);

    List<BookManageSearchResponse> searchBookQuery(String query);

    Page<BookManageSearchResponse> searchAllBooks(Pageable pageable);
}