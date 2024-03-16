package com.example.demo.web.domain.entity;

import com.example.demo.web.domain.enums.AuthorOption;
import jakarta.persistence.*;

@Entity
public class Author extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private AuthorOption authorOption;
}