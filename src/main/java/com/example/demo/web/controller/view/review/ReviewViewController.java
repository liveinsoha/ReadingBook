package com.example.demo.web.controller.view.review;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.response.MyWroteReviewResponse;
import com.example.demo.web.service.MemberService;
import com.example.demo.web.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
@Slf4j
public class ReviewViewController {

    private final ReviewService reviewService;
    private final MemberService memberService;

    @GetMapping
    public String review(Principal principal, Model model){
        Member member = memberService.getMember(principal);
        List<MyWroteReviewResponse> responses = reviewService.findWroteReviews(member);

        model.addAttribute("responses", responses);
        model.addAttribute("active", "review");
        return "user/review/review";
    }


}