package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Book;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookManageSearchResponse {
    private Long bookId;
    private String title;
    private String publisher;
    private String savedImageName;
    private boolean isAccepted;
    private boolean isOnSale;
    private boolean isRequested;

    @QueryProjection
    public BookManageSearchResponse(Book book) {
        this.bookId = book.getId();
        this.title = book.getTitle();
        this.publisher = book.getPublisher();
        this.savedImageName = book.getSavedImageName();
        this.isAccepted = book.isAccepted();
        this.isOnSale = book.isOnSale();
        this.isRequested = book.isRequested();
    }
}