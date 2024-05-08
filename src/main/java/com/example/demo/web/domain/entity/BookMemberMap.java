package com.example.demo.web.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@ToString
@SQLDelete(sql = "UPDATE book_member_map SET deleted = true WHERE book_member_map_id = ?")
@SQLRestriction("deleted = false") // 검색시 deleted = false 조건을 where 절에 추가
public class BookMemberMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_member_map_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    public static BookMemberMap create(Member member, Book book) {
        BookMemberMap bookMemberMap = new BookMemberMap();
        bookMemberMap.member = member;
        bookMemberMap.book = book;
        return bookMemberMap;
    }
}
