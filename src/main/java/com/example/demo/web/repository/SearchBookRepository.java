package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.response.BookManageSearchResponse;
import com.example.demo.web.dto.response.BookSearchResponse;
import com.example.demo.web.service.search.BookSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchBookRepository {
    Page<BookSearchResponse> search(String query, Pageable pageable, BookSearchCondition condition);

    Page<BookManageSearchResponse> searchRegisteredBook(String query, Pageable pageable, BookSearchCondition condition, Member seller);

   // List<BookManageSearchResponse> searchBookQuery(String query);

    List<BookManageSearchResponse> searchBookQuery(String query, Member seller);

    Page<BookManageSearchResponse> searchAllBooks(Pageable pageable, Member seller);
}