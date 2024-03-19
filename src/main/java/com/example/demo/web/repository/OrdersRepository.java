package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Page<Orders> findByMemberId(Pageable pageable, Long memberId);

}