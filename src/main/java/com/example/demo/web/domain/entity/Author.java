package com.example.demo.web.domain.entity;

import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.dto.request.AuthorRegisterRequest;
import com.example.demo.web.dto.request.AuthorUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.web.service.annotation.GetExchange;

@Entity
@Getter
public class Author extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long id;

    private String name;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member registrant;

    @Enumerated(EnumType.STRING)
    private AuthorOption authorOption;

    private String nationality;
    private String description;
    private String birthYear;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public static Author createAuthor(AuthorRegisterRequest request) {
        Author author = new Author();
        author.name = request.getName();
        author.authorOption = request.getAuthorOption();
        author.nationality = request.getNationality();
        author.description = request.getDescription();
        author.birthYear = request.getBirthYear();
        author.gender = request.getGender();
        return author;
    }

    public void updateAuthor(AuthorUpdateRequest request) {
        this.name = request.getName();
        this.authorOption = request.getAuthorOption();
        this.nationality = request.getNationality();
        this.description = request.getDescription();
    }
}