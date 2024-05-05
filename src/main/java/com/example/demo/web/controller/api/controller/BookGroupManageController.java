package com.example.demo.web.controller.api.controller;

import com.example.demo.web.domain.entity.BookGroup;
import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.dto.request.BookGroupRegisterRequest;
import com.example.demo.web.dto.response.BookGroupSearchResponse;
import com.example.demo.web.service.BookGroupManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage/book-group")
public class BookGroupManageController {

    private final BookGroupManageService bookGroupManagementService;

    @PostMapping
    public ResponseEntity<Object> registerCategoryGroup(BookGroupRegisterRequest request,
                                                        MultipartFile file) {
        bookGroupManagementService.registerBookGroup(request, file);

        BaseResponse response = new BaseResponse(HttpStatus.CREATED, "등록이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/image/{bookGroupId}")
    public ResponseEntity<Object> updateImage(MultipartFile file, @PathVariable Long bookGroupId) {
        bookGroupManagementService.updateBookGroupImage(file, bookGroupId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/title/{bookGroupId}")
    public ResponseEntity<Object> updateTitle(@RequestParam String title, @PathVariable Long bookGroupId) {
        bookGroupManagementService.updateBookGroupTitle(title, bookGroupId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{bookGroupId}")
    public ResponseEntity<Object> delete(@PathVariable Long bookGroupId) {
        bookGroupManagementService.delete(bookGroupId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "삭제가 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchBookGroups(@RequestParam("name") String name) {
        List<BookGroupSearchResponse> bookGroupSearchResponses = bookGroupManagementService.searchByBookGroupTitle(name);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "검색이 완료되었습니다.", bookGroupSearchResponses);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}