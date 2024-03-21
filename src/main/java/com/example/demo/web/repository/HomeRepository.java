package com.example.demo.web.repository;

import com.example.demo.web.dto.response.HomeBooksResponse;

import java.util.List;

public interface HomeRepository {

    List<HomeBooksResponse> findBestBooks();
    List<HomeBooksResponse> findNewbooks();
}
