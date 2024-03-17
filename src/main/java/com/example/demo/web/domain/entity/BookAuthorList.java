package com.example.demo.web.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class BookAuthorList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_author_list_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    private Long ordinal; //책의 작가 목록에서 순서를 나타냄.

    public static BookAuthorList createBookAuthorList(Book book, Author author, int ordinal) {
        BookAuthorList bookAuthorList = new BookAuthorList();
        bookAuthorList.book = book;
        bookAuthorList.author = author;
        bookAuthorList.ordinal = Long.valueOf(ordinal);
        return bookAuthorList;
    }
}
