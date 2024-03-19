package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Wishlist;
import lombok.Getter;

@Getter
public class WishlistResponse {
    private Long bookId;
    private Long wishlistId;
    private String title;
    private String isbn;
    private String savedImageName;
    private int ebookPrice;
    private int discountRate;
    private int discountPrice;
    private int salePrice;

    public WishlistResponse(Wishlist wishlist) {
        bookId = wishlist.getBook().getId();
        wishlistId = wishlist.getId();
        title = wishlist.getBook().getTitle();
        isbn = wishlist.getBook().getIsbn();
        savedImageName = wishlist.getBook().getSavedImageName();
        ebookPrice = wishlist.getBook().getEbookPrice();
        discountRate = wishlist.getBook().getDiscountRate();
        discountPrice = (int) (ebookPrice * discountRate * 0.01);
        salePrice = wishlist.getBook().getSalePrice();
    }
}