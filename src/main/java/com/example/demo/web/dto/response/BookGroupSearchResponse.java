package com.example.demo.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookGroupSearchResponse {
    private Long id;
    private String title;
    private String savedImageName;
}