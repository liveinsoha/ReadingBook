package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Review;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewResponse {
    private Long memberId;
    private Long bookId;
    private Long reviewId;
    private String maskedId;
    private boolean isPurchased;
    private String content;
    private String likesCount;
    private int starRating;

    public ReviewResponse(Review review) {
        this.memberId = review.getMember().getId();
        this.bookId = review.getBook().getId();
        this.reviewId = review.getId();
        this.maskedId = createMaskedId(review.getMember().getEmail());
        this.isPurchased = review.isPurchased();
        this.content = review.getContent();
        this.likesCount = createLikesCount(review.getLikesCount());
        this.starRating = review.getStarRating();
    }

    private String createLikesCount(int reviewLikesCount) {
        if(reviewLikesCount > 999){
            return "999+";
        }else{
            return String.valueOf(reviewLikesCount);
        }
    }

    private String createMaskedId(String email) {
        return email.substring(0, 3) + "***";
    }
}
