package com.example.demo.web.controller.api.controller;

import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.service.BookRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class BookRequestController {

    private final BookRequestService bookRequestService;

    public BookRequestController(BookRequestService bookRequestService) {
        this.bookRequestService = bookRequestService;
    }

    @PostMapping("/accept/book/{bookId}")
    public ResponseEntity<Object> acceptBookRequest(@PathVariable Long bookId) {
        // 책 승인 로직을 수행하는 메서드 호출
         bookRequestService.acceptBookRequest(bookId);
        BaseResponse response = new BaseResponse(HttpStatus.OK, "승인이 완료되었습니다.", true);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/reject/book/{bookId}")
    public ResponseEntity<Object> rejectBookRequest(@PathVariable Long bookId) {
        // 책 거부 로직을 수행하는 메서드 호출
        bookRequestService.rejectBookRequest(bookId);
        BaseResponse response = new BaseResponse(HttpStatus.OK, "거부가 완료되었습니다.", true);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}