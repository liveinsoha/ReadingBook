package com.example.demo.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookManageSearchResponse {
    private Long bookId;
    private String title;
    private String publisher;
    private String savedImageName;
}