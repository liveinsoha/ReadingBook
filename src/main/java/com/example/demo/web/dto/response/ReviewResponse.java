package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Review;
import com.example.demo.web.domain.entity.ReviewComment;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
public class ReviewResponse {
    private Long memberId;
    private Long bookId;
    private Long reviewId;
    private String maskedId;
    private boolean isPurchased;
    private String content;
    private String likesCount;
    private int starRating;
    private String commentsCount;
    private List<ReviewCommentResponse> reviewComments;

    public ReviewResponse(Review review) {
        this.memberId = review.getMember().getId();
        this.bookId = review.getBook().getId();
        this.reviewId = review.getId();
        this.maskedId = createMaskedId(review.getMember().getEmail());
        this.isPurchased = review.isPurchased();
        this.content = review.getContent();
        this.likesCount = createLikesCount(review.getLikesCount());
        this.starRating = review.getStarRating();
        this.commentsCount = createCommentCount(review.getCommentsCount());
        this.reviewComments = createReviewCommentResponses(review.getReviewComments());
    }

    private String createLikesCount(int reviewLikesCount) {
        if (reviewLikesCount > 999) {
            return "999+";
        } else {
            return String.valueOf(reviewLikesCount);
        }
    }

    private String createCommentCount(int commentCount) {
        if (commentCount > 999) {
            return "999+";
        } else {
            return String.valueOf(commentCount);
        }
    }

    private List<ReviewCommentResponse> createReviewCommentResponses(List<ReviewComment> comments) {
        if (!comments.isEmpty()) {
            return comments.stream() //여기서 대댓글 DTO를 생성하는데, 대댓글에 대한 데이터가 필요하다
                    .map(ReviewCommentResponse::new)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    private String createMaskedId(String email) {
        return email.substring(0, 3) + "***";
    }
}
