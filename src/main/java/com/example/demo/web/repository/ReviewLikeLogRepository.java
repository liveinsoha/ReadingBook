package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Review;
import com.example.demo.web.domain.entity.ReviewLikeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeLogRepository extends JpaRepository<ReviewLikeLog, Long> {

    boolean existsByReviewAndMember(Review review, Member member);

    Optional<ReviewLikeLog> findByReviewAndMember(Review review, Member member);
}
