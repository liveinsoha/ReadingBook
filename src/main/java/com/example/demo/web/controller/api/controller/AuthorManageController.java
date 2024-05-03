package com.example.demo.web.controller.api.controller;

import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.dto.request.AuthorRegisterRequest;
import com.example.demo.web.dto.request.AuthorUpdateRequest;
import com.example.demo.web.dto.response.AuthorSearchResponse;
import com.example.demo.web.service.AuthorManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage/author")
public class AuthorManageController {

    private final AuthorManageService authorManagementService;

    @PostMapping
    public ResponseEntity<Object> registerAuthor(AuthorRegisterRequest request) {
        authorManagementService.registerAuthor(request);

        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "등록이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{authorId}")
    public ResponseEntity<Object> updateAuthor(AuthorUpdateRequest request,
                                               @PathVariable Long authorId) {
        authorManagementService.updateAuthor(request, authorId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Object> deleteAuthor(@PathVariable Long authorId) {
        authorManagementService.delete(authorId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "삭제가 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }




}