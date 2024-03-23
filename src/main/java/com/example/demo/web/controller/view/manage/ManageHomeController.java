package com.example.demo.web.controller.view.manage;

import com.example.demo.web.dto.response.SaleResponse;
import com.example.demo.web.service.MemberService;
import com.example.demo.web.service.OrdersService;
import com.example.demo.web.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/manage")
public class ManageHomeController {

    private final OrdersService ordersService;
    private final MemberService memberService;
    private final ReviewService reviewService;

    @GetMapping("/home")

    public String home(Model model) {

        SaleResponse sales = ordersService.findSalesOfTodayAndWeekAndMonth();
        model.addAttribute("sales", sales);

        int todayRegisteredMember = memberService.findTodayRegisterMemberCount();
        model.addAttribute("registeredMemberCount", todayRegisteredMember);

        int todayReviewCount = reviewService.findTodayReviewCount();
        model.addAttribute("todayReviewCount", todayReviewCount);
        return "manage/home";
    }
}