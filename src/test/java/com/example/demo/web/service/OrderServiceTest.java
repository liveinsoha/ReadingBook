package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.OrderBooks;
import com.example.demo.web.domain.entity.Orders;
import com.example.demo.web.dto.request.OrderRequest;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.repository.BookRepository;
import com.example.demo.web.repository.MemberRepository;
import com.example.demo.web.repository.OrderBooksRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderBooksRepository orderBooksRepository;

    @Autowired
    private InitClass initClass;

    @Test
    void orderNo(){
        String orderNo = orderService.getOrderNo();
        System.out.println("orderNo = " + orderNo);
        assertThat(orderNo.length()).isEqualTo(14);
    }

    /*--- 주문 잘 들어갔는지 확인 ---*/
    @Test
    void whenOrderedBooks_thenVerifyFields(){
        Member member = memberRepository.save(new Member());
        Long memberId = member.getId();

        List<Book> books = new ArrayList<>();
        int orderCount = 5;
        for(int i = 0; i < orderCount; i++){
            Book book = bookRepository.save(new Book());
            books.add(book);
        }
        List<Long> bookIdList = books.stream().map(b -> b.getId()).collect(Collectors.toList());
        OrderRequest orderRequest = new OrderRequest(
                "test 외 2권", "2023020211215", "test", "card", "test@test", 10000, 5000, 5000,
                bookIdList
        );
        Long orderId = orderService.order(member, books, orderRequest);
        Orders orders = orderService.findOrders(orderId);

        assertThat(orders.getOrderName()).isEqualTo("test 외 2권");
        assertThat(orders.getOrderNo()).isEqualTo("2023020211215");
        assertThat(orders.getImpUid()).isEqualTo("test");
        assertThat(orders.getChoosingOption()).isEqualTo("card");
        assertThat(orders.getEmail()).isEqualTo("test@test");
        assertThat(orders.getOrderAmount()).isEqualTo(10000);
        assertThat(orders.getDiscountAmount()).isEqualTo(5000);
        assertThat(orders.getPaymentAmount()).isEqualTo(5000);

        assertThat(orders.getMember().getId()).isEqualTo(memberId);

        List<OrderBooks> orderBooksList = orderBooksRepository.findByOrdersId(orderId);
        //Order - Book 매핑 클래스 저장 확인. orderId로 찾으면 5개 나옴
        assertThat(orderBooksList.size()).isEqualTo(orderCount);


    }

    @Test
    void when_alreadyPurchasedBook_then_throwError(){
        initClass.initMemberData();
        initClass.initBookAndAuthorData();
        initClass.initOrderData();

        Member member = initClass.getMember(1L);
        Book book1 = initClass.getBook(1L);
        Book book2 = initClass.getBook(2L);
        OrderRequest orderRequest = new OrderRequest(
                "test 외 2권", "2023020211215", "test", "card", "test@test", 10000, 5000, 5000,
                Arrays.asList(1L)
        );

        assertThatThrownBy(() -> orderService.order(member, List.of(book1,book2), orderRequest))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("주문하고자 하는 도서 중 일부를 이미 구입하셨습니다.");
    }


}