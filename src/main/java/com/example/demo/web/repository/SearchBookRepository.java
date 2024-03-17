package com.example.demo.web.repository;

import com.example.demo.web.dto.response.BookSearchResponse;
import com.example.demo.web.service.search.BookSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBookRepository {
    Page<BookSearchResponse> search(String query, Pageable pageable, BookSearchCondition condition);
}