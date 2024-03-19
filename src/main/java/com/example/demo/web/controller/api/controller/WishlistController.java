package com.example.demo.web.controller.api.controller;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.service.BookService;
import com.example.demo.web.service.MemberService;
import com.example.demo.web.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishlistController {

        private final WishlistService wishlistService;
        private final MemberService memberService;
        private final BookService bookService;


        @PostMapping
        public ResponseEntity<Object> addBook(Long bookId, Principal principal){
            Member member = memberService.getMember(principal);
            Book book = bookService.findBook(bookId);
            wishlistService.addBook(member, book);

            BaseResponse response = new BaseResponse(
                    HttpStatus.CREATED, "해당 도서가 위시리스트에 담겼습니다.", true
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);
        }

        @DeleteMapping
        public ResponseEntity<Object> deleteBook(@RequestParam List<Long> wishlistIdList, Principal principal){
            Long memberId = memberService.getMember(principal).getId();
            wishlistService.delete(wishlistIdList, memberId);

            BaseResponse response = new BaseResponse(
                    HttpStatus.OK, "도서를 위시리스트에서 제거했습니다.", true
            );
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        }


}
