package com.example.demo.web.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class BookContent extends BaseEntity {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    private Long id;

    @JoinColumn(name = "book_id")
    private Book book;
    private String content;


    public static BookContent createBookContent(Book book, String content) {
        BookContent bookContent = new BookContent();
        bookContent.book = book;
        bookContent.content = content;
        return bookContent;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}