package com.example.demo.web.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookManageSearchResponse {
    private Long bookId;
    private String title;
    private String publisher;
    private String savedImageName;

    @QueryProjection
    public BookManageSearchResponse(Long bookId, String title, String publisher, String savedImageName) {
        this.bookId = bookId;
        this.title = title;
        this.publisher = publisher;
        this.savedImageName = savedImageName;
    }
}