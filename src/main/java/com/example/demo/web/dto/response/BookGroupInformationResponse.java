package com.example.demo.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookGroupInformationResponse {
    private String isbn;
    private String title;
    private String savedImageName;
}