package com.example.demo.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorSearchResponse {
    private Long authorId;
    private String authorName;
    private String birthYear;
    private String gender;
    private String authorOption;
}