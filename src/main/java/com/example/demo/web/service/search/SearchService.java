package com.example.demo.web.service.search;

import com.example.demo.web.dto.response.BookSearchResponse;
import com.example.demo.web.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SearchService {
    private final BookRepository bookRepository;

    public Page<BookSearchResponse> search(String query, Pageable pageable, BookSearchCondition condition) {
        Page<BookSearchResponse> responses = bookRepository.search(query, pageable, condition);
        return responses;
    }
}