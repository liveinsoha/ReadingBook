package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.QBook;
import com.example.demo.web.domain.entity.QBookAuthorList;
import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.dto.response.HomeBooksResponse;
import com.example.demo.web.dto.response.QHomeBooksResponse;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.example.demo.web.domain.entity.QBook.book;
import static com.example.demo.web.domain.entity.QBookAuthorList.*;
import static com.example.demo.web.domain.entity.QOrderBooks.orderBooks;
import static com.example.demo.web.domain.entity.QReview.review;
import static com.querydsl.jpa.JPAExpressions.select;

@SpringBootTest
class HomeRepositoryImplTest {

    @Autowired
    private EntityManager em;

    @Autowired
    BookRepository bookRepository;

    private JPAQueryFactory queryFactory;


    @BeforeEach
    void beforeAll() {
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    void FindNewBookQueryTest(){
        bookRepository.findNewbooks();
    }

    @Test
    void QueryTest() {

    /*    List<Long> fetch = queryFactory.select(orderBooks.book.id)
                .from(orderBooks)
                .join(orderBooks.book)
                .groupBy(orderBooks.book.id)
                .orderBy(orderBooks.book.reviewCount.count().desc())
                .limit(6).fetch();*/

        //주문 건 수가 가장 많은 책들의 특징들을 가져와 보자


        List<Long> top5Books = queryFactory.select(book.id) //리뷰 개수 순 내림차순
                .from(book)
                .orderBy(book.reviewCount.desc())
                .limit(5)
                .fetch();


        List<HomeBooksResponse> bestBookResponse = queryFactory.select(
                        new QHomeBooksResponse(
                                book.isbn,
                                book.savedImageName,
                                book.title,
                                ExpressionUtils.as(select(bookAuthorList.author.name)
                                        .from(bookAuthorList)
                                        .join(bookAuthorList.author)
                                        .where(
                                                bookAuthorList.book.id.eq(book.id)
                                                        .and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR))
                                                        .and(bookAuthorList.ordinal.eq(1L))), "mainAuthor"),

                                ExpressionUtils.as(select(bookAuthorList.id.count())
                                        .from(bookAuthorList)
                                        .join(bookAuthorList.author)
                                        .where(bookAuthorList.book.id.eq(book.id).and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR))), "authorCount"),

                                ExpressionUtils.as(select(review.starRating.sum()) //책 id 별로 별점 총 합을 가져옴
                                        .from(review)
                                        .join(review.book)
                                        .where(review.book.id.eq(book.id)), "totalStarRating"),

                                book.reviewCount)
                )
                .from(book)
                .where(book.id.in(
                        top5Books))
                .fetch();

        System.out.println("bestBookResponse = " + bestBookResponse);
    }

}