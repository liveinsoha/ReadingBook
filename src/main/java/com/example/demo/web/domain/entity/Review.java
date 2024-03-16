package com.example.demo.web.domain.entity;

import jakarta.persistence.*;


@Entity
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String content;
    private Integer starRating;
    private boolean isPurchased;
    private boolean isHidden;
    private int likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
