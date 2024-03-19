package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.OrderBooks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderBooksRepository extends JpaRepository<OrderBooks, Long> {

    List<OrderBooks> findByOrdersId(Long ordersId);

}