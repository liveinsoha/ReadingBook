package com.example.demo.web.domain.entity;

import jakarta.persistence.*;

@Entity
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_group_id")
    private CategoryGroup categoryGroup;
}