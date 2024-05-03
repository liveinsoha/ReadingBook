package com.example.demo.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;


@Data
public class BookRegisterRequest {
    private String title;
    private String isbn;
    private String publisher;
    private String publishingDate;
    private List<ReqAuthorOrdinal> authors;
    private int paperPrice;
    private int ebookPrice;
    private int discountRate;
    private Long categoryId;
    private Long bookGroupId;
    private String description;

    public BookRegisterRequest(String title, String isbn, String publisher, String publishingDate, int paperPrice, int ebookPrice, int discountRate, Long categoryId, Long bookGroupId, String description) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishingDate = publishingDate;
        this.paperPrice = paperPrice;
        this.ebookPrice = ebookPrice;
        this.discountRate = discountRate;
        this.categoryId = categoryId;
        this.description =description;
        if(bookGroupId == 0){
            this.bookGroupId = null;
        } else{
            this.bookGroupId = bookGroupId;
        }
    }
}