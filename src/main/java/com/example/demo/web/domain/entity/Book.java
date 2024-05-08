package com.example.demo.web.domain.entity;

import com.example.demo.web.dto.request.BookRegisterRequest;
import com.example.demo.web.dto.request.BookUpdateRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@ToString
@SQLDelete(sql = "UPDATE book SET deleted = true WHERE book_id = ?")
@SQLRestriction("deleted = false") // 검색시 deleted = false 조건을 where 절에 추가
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;
    private String title;
    private String isbn;
    private String publisher;
    private String publishingDate;
    private int paperPrice;
    private int ebookPrice;
    private int discountRate;
    private String savedImageName;
    private int salePrice;

    private boolean isOnSale;
    private boolean isAccepted;
    private boolean isRequested;

    @JoinColumn(name = "member_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_group_id")
    @ToString.Exclude
    private BookGroup bookGroup;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean deleted = Boolean.FALSE;

    private int reviewCount;


    public static Book createBook(BookRegisterRequest request, Category category, BookGroup bookGroup, String savedImageName, Member seller) {
        Book book = new Book();
        book.title = request.getTitle();
        book.isbn = request.getIsbn();
        book.publisher = request.getPublisher();
        book.publishingDate = request.getPublishingDate();
        book.paperPrice = request.getPaperPrice();
        book.ebookPrice = request.getEbookPrice();
        book.discountRate = request.getDiscountRate();
        int discountPrice = (int) (request.getEbookPrice() * request.getDiscountRate() * 0.01);
        book.salePrice = request.getEbookPrice() - discountPrice; //판매 가격 추가

        book.isAccepted = false; // 승인 여부
        book.isRequested = true; //판매 요청
        book.isOnSale = false;

        book.seller = seller;

        book.category = category;
        book.bookGroup = bookGroup;
        book.savedImageName = savedImageName;

        book.description = request.getDescription();
        book.reviewCount = 0;
        return book;
    }

    public void updateImage(String updatedImageName) {
        savedImageName = updatedImageName;
    }

    public void updateContent(BookUpdateRequest request, Category category, BookGroup bookGroup) {
        this.title = request.getTitle();
        this.isbn = request.getIsbn();
        this.publisher = request.getPublisher();
        this.publishingDate = request.getPublishingDate();
        this.paperPrice = request.getPaperPrice();
        this.ebookPrice = request.getEbookPrice();
        this.discountRate = request.getDiscountRate();
        this.category = category;
        this.bookGroup = bookGroup;
        int discountPrice = (int) (request.getEbookPrice() * request.getDiscountRate() * 0.01);
        this.salePrice = request.getEbookPrice() - discountPrice; //판매 가격 추가
        this.description = request.getDescription();
    }

    public void addReviewCount() {
        this.reviewCount++;
    }

    public void subtractReviewCount() {
        this.reviewCount--;
    }
}