package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByMemberId(Long memberId);

    Optional<Review> findByMemberIdAndBookId(Long memberId, Long bookId);

    boolean existsByMemberIdAndBookId(Long memberId, Long bookId);

    @Query(
            "select r " +
                    "from Review r " +
                    "join r.member " +
                    "join r.book " +
                   // "left join r.reviewComments " +
                    "where r.book.id = :bookId " +
                    "order by r.likesCount desc, r.createdTime asc "
    )
    List<Review> findReviews(Long bookId);
}