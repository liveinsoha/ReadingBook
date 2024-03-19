package com.example.demo.web.controller.api.controller;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.dto.request.OrderRequest;
import com.example.demo.web.service.BookService;
import com.example.demo.web.service.MemberService;
import com.example.demo.web.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrdersService ordersService;
    private final MemberService memberService;
    private final BookService bookService;

    @PostMapping("/order")
    public ResponseEntity<Object> order(@ModelAttribute OrderRequest request, Principal principal) {
        Member member = memberService.getMember(principal);
        List<Book> books = bookService.findAllById(request.getBookIdList());

        ordersService.order(member, books, request);

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.OK, "주문이 완료되었습니다", true
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(baseResponse);
    }
}
