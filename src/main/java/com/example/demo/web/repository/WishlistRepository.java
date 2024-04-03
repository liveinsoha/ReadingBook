package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    boolean existsByMemberAndBook(Member member, Book book);

    @Query(
            "select w " +
                    "from Wishlist w " +
                    "join w.member m " +
                    "join w.book b " +
                    "where m.id = :memberId"
    )
    List<Wishlist> findByMemberId(@Param("memberId") Long memberId);

    @Query(
            "select w " +
                    "from Wishlist w " +
                    "where w.member.id = :memberId and w.book.id in(:bookIdList)"
    )
    List<Wishlist> findByMemberIdAndBookIds(@Param("memberId") Long memberId, @Param("bookIdList") List<Long> bookIdList);
}
