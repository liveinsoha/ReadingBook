package com.example.demo.web.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.web.service.annotation.GetExchange;


@Entity
@Getter
public class Wishlist extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    public static Wishlist createWishlist(Book book, Member member) {
        Wishlist wishlist = new Wishlist();
        wishlist.book = book;
        wishlist.member = member;
        return wishlist;
    }
}