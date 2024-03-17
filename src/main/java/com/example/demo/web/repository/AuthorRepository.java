package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAllByName(String name);
}