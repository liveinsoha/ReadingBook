package com.example.demo.web.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class BookSearchResponse {
    private Long bookId;
    private String isbn;
    private String savedImageName;
    private String title;
    private String publisher;
    private String description;
    private int salePrice;
    private String author;
    private int authorCountExceptMainAuthor;
    private String translator;
    private String reviewRatingAvg;
    private int reviewCount;

    private String categoryGroupName;

    @QueryProjection
    public BookSearchResponse(Long bookId, String isbn, String savedImageName, String title,
                              String publisher, String description, int ebookPrice, int discountRate,
                              String author, Long authorCount, String translator,
                              String categoryGroupName, int totalStarRating, int reviewCount) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.savedImageName = savedImageName;
        this.title = title;
        this.publisher = publisher;
        this.description = cutDescription(description);
        this.salePrice = calculateSalePrice(ebookPrice, discountRate);
        this.author = author;
        if (authorCount == null) {
            this.authorCountExceptMainAuthor = 0;
        } else {
            this.authorCountExceptMainAuthor = authorCount.intValue() - 1;
        }
        this.translator = translator;
        this.categoryGroupName = categoryGroupName;
        this.reviewRatingAvg = calculateReviewAvg(totalStarRating, reviewCount); //모든 평점 합
        this.reviewCount = reviewCount; //리뷰 수
    }

    private String calculateReviewAvg(int totalReviewRating, int reviewCount) {
        if (totalReviewRating == 0) {
            return "0";
        }
        double avg = (double) totalReviewRating / reviewCount;
        return String.format("%.1f", avg);
    }

    private int calculateSalePrice(int ebookPrice, int discountRate) {
        int discountPrice = (int) (ebookPrice * discountRate * 0.01);
        return ebookPrice - discountPrice;
    }

    private String cutDescription(String description) {
        if (description.length() > 200) {
            return description.substring(0, 200) + "...";
        }
        return description.substring(0, description.length() - 1) + "...";
    }
}