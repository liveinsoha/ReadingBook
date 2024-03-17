package com.example.demo.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategorySearchResponse {
    private Long categoryGroupId;
    private Long categoryId;
    private String categoryName;
    private String categoryGroupName;
    private boolean isSearched;
}
