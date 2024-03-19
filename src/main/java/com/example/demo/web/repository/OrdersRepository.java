package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}