package com.example.demo.web.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * 구메라는 행위에 대하여 책과 회원은 다대다 매핑
 * 멤버 - 책 구매한 책들에 관한 매핑 클래스
 */
@Entity
@Getter
public class Library extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "library_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    public static Library createLibrary(Member member, Book book) {
        Library library = new Library();
        library.member = member;
        library.book = book;
        return library;
    }
}