package com.example.demo.web.controller.api.controller;

import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.dto.request.BookAuthorListRegisterRequest;
import com.example.demo.web.service.BookAuthorListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage/book-author-list")
public class BookAuthorListController {
    private final BookAuthorListService bookAuthorListService;

    /**
     * 책 ,작가를 각각 미리 등록한 상태에서, 도서 작가 등록 을 통해책-작가 매핑 클래스를 등록하다.
     * 책과 도서가 먼저 있어야한다 -> 책을 등록할 경우 작가를 등록하는 형태로 바꿔볼 수 있다.
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<Object> registerBookAuthorList(BookAuthorListRegisterRequest request){
        bookAuthorListService.register(request);

        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "등록이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteBookAuthorList(Long bookId, Long authorId){
        bookAuthorListService.delete(bookId, authorId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "제거가 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}