package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Book;
import lombok.Getter;

@Getter
public class OrderBooksResponse {
    private String title;
    private String isbn;

    public OrderBooksResponse(Book book) {
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
    }
}