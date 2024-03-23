package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Review;
import com.example.demo.web.domain.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Modifying
    @Query("DELETE " +
            "FROM ReviewComment rc " +
            "where rc.review in (:reviews)")
    void mDeleteAllByReviewInQuery(List<Review> reviews);

    @Modifying
    @Query("DELETE " +
            "FROM ReviewComment rc " +
            "where rc.review = :review")
    void mDeleteByReview(Review review);


    @Modifying //수정하는 쿼리임을 의미
    @Query("DELETE " +
            "FROM ReviewComment rc " +
            "WHERE rc.member = :member")
    void mDeleteByMember(Member member);
}
