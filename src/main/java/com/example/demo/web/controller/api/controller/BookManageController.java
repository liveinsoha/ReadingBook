package com.example.demo.web.controller.api.controller;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.dto.request.BookRegisterRequest;
import com.example.demo.web.dto.request.BookUpdateRequest;
import com.example.demo.web.dto.response.AuthorSearchResponse;
import com.example.demo.web.service.AuthorManageService;
import com.example.demo.web.service.BookManageService;
import com.example.demo.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage/book")
public class BookManageController {

    private final BookManageService bookManagementService;
    private final AuthorManageService authorManageService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Object> registerBook(Principal principal,
                                               BookRegisterRequest request,
                                               MultipartFile file) {

        Member member = memberService.getMember(principal);

        System.out.println("request.getBookGroupId() = " + request.getBookGroupId());
        Long savedBookId = bookManagementService.registerBook(request, file, member.getId());

        BaseResponse<Long> response = new BaseResponse<>(HttpStatus.CREATED, "책 등록 성공! 내용을 등록해주세요", savedBookId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/image/{bookId}")
    public ResponseEntity<Object> updateImage(MultipartFile file, @PathVariable Long bookId) {
        bookManagementService.updateBookImage(file, bookId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/on-sale/{bookId}")
    public ResponseEntity<Object> onSale(Principal principal, @PathVariable Long bookId) {
        Member member = memberService.getMember(principal);
        bookManagementService.updateOnSale(member, bookId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "판매 개시 처리 완료", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/off-sale/{bookId}")
    public ResponseEntity<Object> offSale(Principal principal, @PathVariable Long bookId) {
        Member member = memberService.getMember(principal);
        bookManagementService.updateOffSale(member, bookId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "판매 중단 처리 완료", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/content/{bookId}")
    public ResponseEntity<Object> updateImage(BookUpdateRequest request, @PathVariable Long bookId) {
        bookManagementService.updateBookContent(request, bookId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Object> delete(@PathVariable Long bookId) {
        bookManagementService.deleteBook(bookId);

        log.info("삭제 진입");
        BaseResponse response = new BaseResponse(HttpStatus.OK, "삭제가 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/search/author")
    public ResponseEntity<BaseResponse<List<AuthorSearchResponse>>> returnSearchResult(@RequestParam String name) {
        List<AuthorSearchResponse> authorSearchResponses = authorManageService.searchByAuthorName(name);

        BaseResponse<List<AuthorSearchResponse>> response = new BaseResponse<>(HttpStatus.OK, "검색 완료되었습니다.", authorSearchResponses);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}