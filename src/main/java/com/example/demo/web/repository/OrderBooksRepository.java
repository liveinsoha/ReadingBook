package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.OrderBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderBooksRepository extends JpaRepository<OrderBooks, Long> {


    @Query("select ob " +  //ORDER 테이블과 BOOK 테이블의 컬럼을 쓰지 않을 거면 조인은 왜 하는 거지?
            "from OrderBooks ob " +
            "join ob.orders " +
            "join ob.book " +
            "where ob.orders.id = :ordersId")
    List<OrderBooks> findByOrdersId(@Param("ordersId") Long ordersId);

}