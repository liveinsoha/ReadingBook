package com.example.demo.web.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class BookAuthorSearchResponse {
    private Long bookId;
    private String mainAuthorName;
    private int authorCount;
    private String mainTranslator;

    @QueryProjection
    public BookAuthorSearchResponse(Long bookId, String mainAuthorName, int authorCount, String mainTranslator) {
        this.bookId = bookId;
        this.mainAuthorName = mainAuthorName;
        this.authorCount = authorCount;
        this.mainTranslator = mainTranslator;
    }
}