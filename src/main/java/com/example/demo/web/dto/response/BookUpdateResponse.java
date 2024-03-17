package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookUpdateResponse {
    private Long id;
    private String title;
    private String isbn;
    private String publisher;
    private String publishingDate;
    private int paperPrice;
    private int ebookPrice;
    private int discountRate;
    private String savedImageName;
    private Long categoryId;
    private Long bookGroupId;

    public BookUpdateResponse(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
        this.publisher = book.getPublisher();
        this.publishingDate = book.getPublishingDate();
        this.paperPrice = book.getPaperPrice();
        this.ebookPrice = book.getEbookPrice();
        this.discountRate = book.getDiscountRate();
        this.savedImageName = book.getSavedImageName();
        this.categoryId = book.getCategory().getId();
        if(book.getBookGroup() == null){
            this.bookGroupId = null;
        }else{
            this.bookGroupId = book.getBookGroup().getId();
        }
    }
}