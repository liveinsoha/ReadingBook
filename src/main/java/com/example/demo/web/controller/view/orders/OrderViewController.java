package com.example.demo.web.controller.view.orders;

import com.example.demo.web.dto.response.OrderBooksResponse;
import com.example.demo.web.dto.response.OrderFindResponse;
import com.example.demo.web.dto.response.OrderHistoryResponse;
import com.example.demo.web.dto.response.paging.PagingOrdersDto;
import com.example.demo.web.service.MemberService;
import com.example.demo.web.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
@Slf4j
public class OrderViewController {

    private final MemberService memberService;
    private final OrdersService ordersService;

    @GetMapping("/history")
    public String history(@PageableDefault(size = 15) Pageable pageable, Principal principal,
                          Model model){
        String email = principal.getName();
        Long memberId = memberService.getMember(email).getId();

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "createdTime");

        Page<OrderHistoryResponse> responses = ordersService.getHistory(memberId, pageRequest);
        PagingOrdersDto paging = new PagingOrdersDto(responses);
        model.addAttribute("responses", responses);
        model.addAttribute("paging", paging);
        return "user/orders/history";
    }

    @GetMapping("/{ordersId}")
    public String home(@PathVariable Long ordersId, Model model){
        OrderFindResponse response = ordersService.getOrderDetail(ordersId);
        List<OrderBooksResponse> books = ordersService.getOrderBooks(ordersId);

        model.addAttribute("response", response);
        model.addAttribute("books", books);
        return "user/orders/detail";
    }
}