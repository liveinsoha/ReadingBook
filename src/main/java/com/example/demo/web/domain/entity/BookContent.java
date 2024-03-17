package com.example.demo.web.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class BookContent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_content_id")
    private Long id;


    @JoinColumn(name = "book_id")
    @OneToOne(fetch = FetchType.LAZY)
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