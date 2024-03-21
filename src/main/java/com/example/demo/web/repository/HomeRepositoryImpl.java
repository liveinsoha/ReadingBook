package com.example.demo.web.repository;

import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.dto.response.HomeBooksResponse;
import com.example.demo.web.dto.response.QHomeBooksResponse;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.demo.web.domain.entity.QAuthor.author;
import static com.example.demo.web.domain.entity.QBook.book;
import static com.example.demo.web.domain.entity.QBookAuthorList.bookAuthorList;
import static com.example.demo.web.domain.entity.QOrderBooks.orderBooks;
import static com.example.demo.web.domain.entity.QReview.review;
import static com.querydsl.jpa.JPAExpressions.select;

public class HomeRepositoryImpl implements HomeRepository {

    private final JPAQueryFactory queryFactory;

    public HomeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<HomeBooksResponse> findBestBooks() {

        String subQuery = queryFactory.select(author.name)
                .from(bookAuthorList)
                .join(bookAuthorList.author, author)
                .where(bookAuthorList.book.id.eq(book.id)
                        .and(author.authorOption.eq(AuthorOption.AUTHOR)))
                .fetchFirst(); //fetch 하면 서브쿼리로 활용 x

        JPQLQuery<Long> limitAuthorQuery = select(bookAuthorList.ordinal.min()) //작가 서수 중 가장 작은 것만 색출
                .from(bookAuthorList)  // 책 작가 매핑 테이블 (다)
                .join(bookAuthorList.author) // 작가 테이블 조인(일)
                .where(bookAuthorList.book.id.eq(book.id).and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR)));
        // 책id 같아야 함 . 작가 옵션은 작가여야 함.

        /*JPQLQuery<Long> bestBooksQuery = select(orderBooks.book.id) //Book id 컬럼 가져온다.
                .from(orderBooks)
                .join(orderBooks.book) //BOOK 테이블과 조인, 다(ORDERBOOK)를 기준으로 조인이 일어남.
                .groupBy(orderBooks.book.id) // 책 id 별로 집계
                .orderBy(orderBooks.book.reviewCount.count().desc()) //책 id 별로 리뷰 수 기준 내림차순 정렬
                .limit(6); //위에서 6개*/ //서브쿼리에서 limit 안 됌

        JPQLQuery<Long> bestBookQuery = select(book.id) //리뷰 개수 순 내림차순
                .from(book)
                .orderBy(book.reviewCount.desc())
                .limit(6); // 서브쿼리에서 limit 안 됨
        // 6개로 제한*/

        JPQLQuery<Integer> totalStarRatingSubQuery = queryFactory.select(review.starRating.sum()) //총 별점
                .from(review)
                .where(review.book.id.eq(orderBooks.book.id));

        return queryFactory.select(
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
                        select(book.id) //리뷰 개수 순 내림차순
                                .from(book)
                                .orderBy(book.reviewCount.desc())))
                .limit(5)
                .fetch();
    }

    @Override
    public List<HomeBooksResponse> findNewbooks() {
        JPQLQuery<Long> limitAuthorQuery = select(bookAuthorList.ordinal.min())
                .from(bookAuthorList)
                .join(bookAuthorList.author)
                .where(bookAuthorList.book.id.eq(book.id).and(bookAuthorList.author.authorOption.eq(AuthorOption.AUTHOR)));

        return queryFactory
                .select(new QHomeBooksResponse(
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
                .orderBy(book.createdTime.desc())
                .limit(5)
                .fetch();
    }
}
