package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Wishlist, Long> {
    boolean existsByBookId(Long bookId);
}
