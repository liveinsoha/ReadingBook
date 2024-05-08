package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.dto.response.BookManageSearchResponse;
import com.example.demo.web.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BookAcceptService {

    private final BookRepository bookRepository;

    public List<BookManageSearchResponse> getRequestedBooks() {
        List<Book> requestedBooks = bookRepository.findByIsRequested(true);

        return null;
    }
}
