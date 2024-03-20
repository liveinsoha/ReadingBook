package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Review;
import com.example.demo.web.dto.response.ReviewResponse;
import com.example.demo.web.repository.BookRepository;
import com.example.demo.web.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    private ReviewCommentService reviewCommentService;

    @Autowired
    EntityManager entityManager;

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

    @Test
    void when_ReviewUpdated_then_VerifyField() {
        initClass.initMemberData();
        initClass.initBookAndAuthorData();
        initClass.initOrderData();

        Member member = initClass.getMember(1L);
        Book book = initClass.getBook(1L);
        Long savedReviewId = reviewService.review(member, book, "testReviewContent", 5);
        Review review = reviewService.findReview(savedReviewId);

        assertThat(review.getMember().getId()).isEqualTo(member.getId());
        assertThat(review.getBook().getId()).isEqualTo(book.getId());
        assertThat(review.getContent()).isEqualTo("testReviewContent");

        reviewService.update(member, savedReviewId, "testUpdateReviewContent", 3);
        Review updatedReview = reviewService.findReview(savedReviewId);

        assertThat(updatedReview.getMember().getId()).isEqualTo(member.getId());
        assertThat(updatedReview.getBook().getId()).isEqualTo(book.getId());
        assertThat(updatedReview.getContent()).isEqualTo("testUpdateReviewContent");

    }

    @Test
    void when_ReviewDeleted_then_ReviewNotExisted() {
        initClass.initMemberData();
        initClass.initBookAndAuthorData();
        initClass.initOrderData();

        Member member = initClass.getMember(1L);
        Book book = initClass.getBook(1L);
        Long savedReviewId = reviewService.review(member, book, "testReviewContent", 5);
        List<ReviewResponse> reviews = reviewService.findReviews(book.getId());
        assertThat(reviews.size()).isEqualTo(1);

        reviewService.delete(member, savedReviewId);
        List<ReviewResponse> emptyReviews = reviewService.findReviews(book.getId());
        assertThat(emptyReviews.size()).isEqualTo(0);
    }

    @Test
    void when_ReviewCommentRegistered_then_verifyField() {
        /*--- 초기 데이터 저장 ---*/
        initClass.initMemberData();
        initClass.initBookAndAuthorData();
        initClass.initOrderData();

        Book book = initClass.getBook(1L);
        long ten = 10;
        /*--- 책 1개에 대해 리뷰 10개 작성 ---*/ // 1~10번 회원은 리뷰를 작성
        for (long i = 1; i <= 10; i++) {
            Member reviewWriter = initClass.getMember(i);
            Long savedReviewId = reviewService.review(reviewWriter, book, "Review Content", 5);

            /*--- 각각 리뷰에 대해 모두 다른 회원이 대댓글 10개씩 작성 ---*/
            for (long j = 1; j <= 10; j++) { //11 ~ 110번 회원은 대댓글을 작성
                Long reviewCommentWriterId = ten + j;
                Member reviewCommentWriter = initClass.getMember(reviewCommentWriterId);
                Review review = reviewService.findReview(savedReviewId);
                reviewCommentService.comment(reviewCommentWriter, review, "Review Comment Content" + reviewCommentWriterId);
            }
            ten += 10;
        }
        /*--- 영속성 컨텍스트 초기화 ---*/
        entityManager.flush();
        entityManager.clear();



        /*--- 해당 책에 있는 리뷰들을 불러온다. ---*/
        List<ReviewResponse> reviews = reviewService.findReviews(book.getId());
        for (int i = 0; i <= 9; i++) {
            ReviewResponse reviewResponse = reviews.get(i);
            /*--- 각 reviewResponse내에 대댓글 개수 10개인지 확인 ---*/
            assertThat(reviewResponse.getReviewComments().size()).isEqualTo(10);
            System.out.println(reviewResponse);
        }
    }


}