package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByMemberId(Long memberId);

    @Override
    void deleteAll(Iterable<? extends Review> reviews);


    @Query(
            "select r " +
                    "from Review r " +
                    "where r.member.id = :memberId " +
                    "and r.book.id = :bookId"
    )
    Optional<Review> findByMemberIdAndBookId(Long memberId, Long bookId);

    Optional<Review> findByMemberAndBook(Member member, Book book);

    boolean existsByMemberIdAndBookId(Long memberId, Long bookId);

    @Query(
            "select r " +
                    "from Review r " +
                    "join fetch r.member " +
                    //  "left join fetch r.reviewComments " + // 컬렉션을 조인할 경우 페이징이 불가!
                    "where r.book.id = :bookId " +
                    "order by r.likesCount desc, r.createdTime asc "
    )
    List<Review> findReviewsByBookId(Long bookId);

    List<Review> findAllByMember(Member member);


}