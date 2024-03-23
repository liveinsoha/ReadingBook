package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Page<Orders> findByMemberId(Pageable pageable, Long memberId);

    @Modifying
    @Query(
            "update Orders o " +
                    "set o.member = null " +
                    "where o.member = :member"
    )
    void bulkMemberIdNull(@Param("member") Member member);

    List<Orders> findByCreatedTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Orders> findAllByMember(Member member);

}