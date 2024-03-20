package com.example.demo.web.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class ReviewComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_comment_id")
    private Long id;

    private String content;
    private boolean isHidden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public static ReviewComment createReviewComment(Member member, Review review, String content) {
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.member = member;
        reviewComment.review = review;
        reviewComment.content = content;
        reviewComment.isHidden = false;
        review.addReviewComment(reviewComment); //연관관계 메서드
        review.increaseCommentsCount();
        return reviewComment;
    }
}