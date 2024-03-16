package com.example.demo.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CategoryUpdateRequest {
    private String name;
    private Long categoryGroupId;
}