package com.example.demo.web.controller.view.review;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Review;
import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.service.MemberService;
import com.example.demo.web.service.ReviewCommentService;
import com.example.demo.web.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review-comment")
@Slf4j
public class ReviewCommentController {
    private final ReviewCommentService reviewCommentService;
    private final MemberService memberService;
    private final ReviewService reviewService;
    @PostMapping
    public ResponseEntity<Object> comment(Principal principal, Long reviewId, String content){
        Member member = memberService.getMember(principal);
        Review review = reviewService.findReview(reviewId);

        reviewCommentService.comment(member, review, content);

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.CREATED, "댓글이 작성되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(baseResponse);
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(Principal principal, Long reviewCommentId){
        Member member = memberService.getMember(principal);

        reviewCommentService.delete(member, reviewCommentId);

        BaseResponse baseResponse = new BaseResponse(
                HttpStatus.CREATED, "댓글이 삭제되었습니다.", true
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(baseResponse);
    }
}