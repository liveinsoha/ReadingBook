package com.example.demo.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryGroupSearchResponse {
    private Long id;
    private String name;
    private boolean isSearched;
}