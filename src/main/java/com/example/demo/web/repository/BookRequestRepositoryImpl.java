package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.QBook;
import com.example.demo.web.domain.entity.QBookContent;
import com.example.demo.web.domain.entity.QMember;
import com.example.demo.web.dto.response.QRequestedBookDto;
import com.example.demo.web.dto.response.RequestedBookDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.demo.web.domain.entity.QAuthor.author;
import static com.example.demo.web.domain.entity.QBook.book;
import static com.example.demo.web.domain.entity.QBookContent.bookContent;
import static com.example.demo.web.domain.entity.QMember.member;

@Repository
public class BookRequestRepositoryImpl implements BookRequestRepository {

    private final JPAQueryFactory queryFactory;

    public BookRequestRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<RequestedBookDto> findRequestedBooks(Pageable pageable) {
        JPAQuery<Long> countQuery = queryFactory
                .select(book.count())
                .from(book)
                .where(book.isRequested);

        List<RequestedBookDto> content = queryFactory.select(new QRequestedBookDto(
                        book,
                        bookContent,
                        member
                )).from(bookContent)
                .join(bookContent.book, book) // 수정된 부분
                .join(book.seller, member)
                .where(book.isRequested)
                .orderBy(book.createdTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
