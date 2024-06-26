package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Review;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class MyWroteReviewResponse {
    private Long reviewId;
    private Long bookId;
    private String title;
    private String content;
    private int starRating;
    private String wroteDate;


    public MyWroteReviewResponse(Review review) {
        this.reviewId = review.getId();
        this.bookId = review.getBook().getId();
        this.title = review.getBook().getTitle();
        this.content = review.getContent();
        this.starRating = review.getStarRating();
        wroteDate = createWroteDate(review.getCreatedTime());
    }

    private String createWroteDate(LocalDateTime currentDate) {
        int year = currentDate.getYear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        String month = currentDate.format(formatter);
        int day = currentDate.getDayOfMonth();
        return year + "." + month + "." + day + ".";
    }
}