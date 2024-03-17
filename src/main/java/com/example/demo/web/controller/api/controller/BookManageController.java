package com.example.demo.web.controller.api.controller;

import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.dto.request.BookRegisterRequest;
import com.example.demo.web.dto.request.BookUpdateRequest;
import com.example.demo.web.service.BookManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage/book")
public class BookManageController {

    private final BookManageService bookManagementService;

    @PostMapping
    public ResponseEntity<Object> registerBook(BookRegisterRequest request,
                                               MultipartFile file){
        bookManagementService.registerBook(request, file);

        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "등록이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/image/{bookId}")
    public ResponseEntity<Object> updateImage(MultipartFile file, @PathVariable Long bookId){
        bookManagementService.updateBookImage(file, bookId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/content/{bookId}")
    public ResponseEntity<Object> updateImage(BookUpdateRequest request, @PathVariable Long bookId){
        bookManagementService.updateBookContent(request, bookId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Object> delete(@PathVariable Long bookId){
        bookManagementService.deleteBook(bookId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "삭제가 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}