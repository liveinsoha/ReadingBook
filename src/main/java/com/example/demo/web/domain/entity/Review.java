package com.example.demo.web.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderBook과 비슷한 양상 (다대다 -> 매핑 클래스). but 컨텐츠 있음, 다른 컬럼 있음.
 */
@Getter
@Entity
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String content;
    private Integer starRating;
    private boolean isPurchased;
    private boolean isHidden;
    private int likesCount;
    private int commentsCount;

    @OneToMany(mappedBy = "review")
    private List<ReviewComment> reviewComments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "review")
    private List<ReviewLikeLog> logs = new ArrayList<>();

    public void addReviewComment(ReviewComment reviewComment) {
        reviewComments.add(reviewComment);
    }

    public void increaseCommentsCount() {
        this.commentsCount++;
    }

    public void subtractCommentsCount() {
        this.commentsCount--;
    }

    public void subtractLikesCount() {
        this.likesCount--;
    }

    public void addLikesCount() {
        this.likesCount++;
    }

    public static Review createReview(Member member, Book book, String content, int starRating, boolean isPurchased) {
        Review review = new Review();
        review.member = member;
        review.book = book;
        review.content = content;
        review.starRating = starRating;
        review.likesCount = 0;
        review.isHidden = false;
        review.isPurchased = isPurchased;
        review.commentsCount = 0;
        return review;
    }

    public void update(String content, int starRating) {
        this.content = content;
        this.starRating = starRating;
    }
}
