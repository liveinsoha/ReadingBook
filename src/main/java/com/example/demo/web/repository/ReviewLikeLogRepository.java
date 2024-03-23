package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Review;
import com.example.demo.web.domain.entity.ReviewLikeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeLogRepository extends JpaRepository<ReviewLikeLog, Long> {

    boolean existsByReviewAndMember(Review review, Member member);

    @Modifying
    @Query("DELETE " +
            "FROM ReviewLikeLog rll " +
            "WHERE rll.review in (:reviews)")
    void mDeleteAllByReviewsInQuery(List<Review> reviews);


    @Modifying
    @Query("DELETE " +
            "FROM ReviewLikeLog rll " +
            "WHERE rll.member = :member")
    void mDeleteByMember(Member member);


    @Modifying
    @Query("DELETE " +
            "FROM ReviewLikeLog rll " +
            "WHERE rll.review = :review")
    void mDeleteByReview(Review review);

    List<ReviewLikeLog> findByMember(Member member);

    Optional<ReviewLikeLog> findByReviewAndMember(Review review, Member member);
}
