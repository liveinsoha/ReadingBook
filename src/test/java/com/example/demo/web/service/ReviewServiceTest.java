package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Review;
import com.example.demo.web.repository.BookRepository;
import com.example.demo.web.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReviewServiceTest {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private InitClass initClass;

    @Test
    void whenReviewed_thenVerifyFields() {
        Member member = new Member();
        Member savedMember = memberRepository.save(member);

        Book book = new Book();
        Book savedBook = bookRepository.save(book);

        Long reviewId = reviewService.review(savedMember, savedBook, "testReviewContent", 5);
        Review review = reviewService.findReview(reviewId);

        assertThat(review.getId()).isEqualTo(reviewId);
        assertThat(review.getMember().getId()).isEqualTo(savedMember.getId());
        assertThat(review.getBook().getId()).isEqualTo(savedBook.getId());
        assertThat(review.getContent()).isEqualTo("testReviewContent");
        assertThat(review.getStarRating()).isEqualTo(5);
        assertThat(review.isPurchased()).isFalse();
        assertThat(review.isHidden()).isFalse();
        assertThat(review.getLikesCount()).isEqualTo(0);
        assertThat(savedBook.getReviewCount()).isEqualTo(1);
    }

    @Test
    void when_memberPurchasedBook_then_isPurchasedIsTrue() {
        initClass.initMemberData();
        initClass.initBookAndAuthorData();
        initClass.initOrderData();

        Member member = initClass.getMember(1L);
        Book book = initClass.getBook(1L);

        Long savedReviewId = reviewService.review(member, book, "testReviewContent", 5);
        Review review = reviewService.findReview(savedReviewId);

        assertThat(review.isPurchased()).isTrue();
    }


}