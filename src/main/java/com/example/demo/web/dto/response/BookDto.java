package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Book;
import lombok.Data;
import lombok.Getter;


@Data
public class BookDto {
    private String isbn;
    private Long bookId;
    private String categoryGroupName;
    private String categoryName;
    private String title;
    private String savedImageName;
    private String publisher;
    private int ebookPrice;
    private int paperPrice;
    private int salePrice;
    private String description;
    private int reviewCount;

    public BookDto(Book book) {
        isbn = book.getIsbn();
        bookId = book.getId();
        title = book.getTitle();
        categoryGroupName = book.getCategory().getCategoryGroup().getName();
        categoryName = book.getCategory().getName();
        savedImageName = book.getSavedImageName();
        publisher = book.getPublisher();
        ebookPrice = book.getEbookPrice();
        paperPrice = book.getPaperPrice();
        salePrice = book.getSalePrice();
        description = book.getDescription();
        reviewCount = book.getReviewCount();
    }
}