package com.example.demo.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class BookContentRegisterRequest {
    private Long bookId;
    private String content;
}