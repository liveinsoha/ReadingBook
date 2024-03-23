package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Review;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LikeServiceTest {

    @Autowired
    InitClass initClass;

    @Autowired
    LikeService likeService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    EntityManager entityManager;

    @Test
    void when_LikeAdded_then_verify_likesCounts() {
        initClass.initMemberDataSmall();
        initClass.initBookAndAuthorData();
        initClass.initOrderData(); //1번 회원이 책1, 책2 구매.
        Member member = initClass.getMember(1L);
        Book book = initClass.getBook(1L);

        Long savedReviewId = reviewService.review(member, book, "Review Content", 5);

        // 좋아요 누름
        Member member2 = initClass.getMember(2L);
        likeService.like(member2, savedReviewId);

        entityManager.flush();
        entityManager.clear();

        //좋아요 개수 확인
        Review review = reviewService.findReview(savedReviewId);
        assertThat(review.getLikesCount()).isEqualTo(1);

        likeService.like(member2, savedReviewId);

        //Review review2 = reviewService.findReview(savedReviewId);
        assertThat(review.getLikesCount()).isEqualTo(0);

    }
}