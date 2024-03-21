package com.example.demo.web.service;

import com.example.demo.web.dto.response.HomeBooksResponse;
import com.example.demo.web.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HomeService {
    private final BookRepository bookRepository;


    public List<HomeBooksResponse> findBestBooks() {
        return bookRepository.findBestBooks();
    }

    public List<HomeBooksResponse> findNewBooks() {
        return bookRepository.findNewbooks();
    }
}
