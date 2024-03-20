package com.example.demo.web.controller.view.review;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.service.BookService;
import com.example.demo.web.service.MemberService;
import com.example.demo.web.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;
    private final MemberService memberService;
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Object> review(Principal principal, Long bookId, String content, Double starRating){
        Member member = memberService.getMember(principal);
        Book book = bookService.findBook(bookId);
        int intStarRating = starRating.intValue();
        reviewService.review(member, book, content, intStarRating);

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.CREATED, "리뷰가 작성되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(baseResponse);
    }
}