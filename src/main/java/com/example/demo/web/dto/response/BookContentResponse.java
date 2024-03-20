package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.BookContent;
import lombok.Data;
import lombok.Getter;

@Data
public class BookContentResponse {
    private String title;
    private String content;
    private String isbn;

    public BookContentResponse(BookContent bookContent) {
        title = bookContent.getBook().getTitle();
        content = bookContent.getContent();
        isbn = bookContent.getBook().getIsbn();
    }
}