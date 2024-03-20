package com.example.demo.web.controller.api.controller;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.service.LikeService;
import com.example.demo.web.service.MemberService;
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
@RequestMapping("/like")
@Slf4j
public class LikeController {

    private final LikeService likeService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Object> like(Principal principal, Long reviewId) {
        Member member = memberService.getMember(principal);

        likeService.like(member, reviewId);

        BaseResponse response = new BaseResponse(
                HttpStatus.OK, "좋아요 처리 되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
