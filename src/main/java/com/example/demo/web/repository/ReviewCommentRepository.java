package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    @Query(
            "select rc " +
                    "from ReviewComment rc " +
                    "join fetch rc.member " +
                    "join fetch rc.review " +
                    "where rc.id = :reviewCommentId"
    )
    Optional<ReviewComment> findReviewComment(@Param("reviewCommentId") Long reviewCommentId);
}
