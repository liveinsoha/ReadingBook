package com.example.demo.web.controller.api.controller;

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
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping
    public ResponseEntity<Object> update(Principal principal, Long reviewId, String content, Double starRating){
        Member member = memberService.getMember(principal);
        int intStarRating = starRating.intValue();

        reviewService.update(member, reviewId, content, intStarRating);

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.OK, "리뷰가 수정되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(baseResponse);
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(Principal principal, Long reviewId){
        Member member = memberService.getMember(principal);

        reviewService.delete(member, reviewId);

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.OK, "리뷰가 삭제되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(baseResponse);
    }
}