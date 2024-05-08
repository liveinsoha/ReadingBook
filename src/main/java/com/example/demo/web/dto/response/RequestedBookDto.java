package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.BookContent;
import com.example.demo.web.domain.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class RequestedBookDto {

    private Long bookId;
    private String title;
    private String publisher;
    private String savedImageName;
    private String sellerName;

    private String bookContent;


    @QueryProjection
    public RequestedBookDto(Book book, BookContent bookContent, Member seller) {
        this.bookId = book.getId();
        this.title = book.getTitle();
        this.publisher = book.getPublisher();
        this.savedImageName = book.getSavedImageName();
        this.sellerName = seller.getName();
        this.bookContent = bookContent.getContent();
    }

}
