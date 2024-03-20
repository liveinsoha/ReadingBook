package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class MyWroteReviewInBookResponse {
    private Long reviewId;
    private String content;
    private int starRating;
    private String wroteDate;

    public MyWroteReviewInBookResponse(Review review) {
        reviewId = review.getId();
        content = review.getContent();
        starRating = review.getStarRating();
        LocalDateTime createdTime = review.getCreatedTime();
        wroteDate = createWroteDate(createdTime);
    }

    private String createWroteDate(LocalDateTime currentDate) {
        int year = currentDate.getYear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        String month = currentDate.format(formatter);
        int day = currentDate.getDayOfMonth();
        return year + "." + month + "." + day + ".";
    }
}