package com.example.demo.web.controller.view.wishlist;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.response.WishlistResponse;
import com.example.demo.web.service.MemberService;
import com.example.demo.web.service.WishlistService;
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
@RequestMapping("/wishlist")
@Slf4j
public class WishlistViewController {

    private final WishlistService wishlistService;
    private final MemberService memberService;

    @GetMapping
    public String wishlist(Principal principal, Model model){
        if(principal == null){
            return "login/login";
        }

        String email = principal.getName();
        Member member = memberService.getMember(email);
        Long memberId = member.getId();

        List<WishlistResponse> responses = wishlistService.findBookResponses(memberId);
        int totalPrice = 0;
        int totalDiscountPrice = 0;
        int totalSalePrice = 0;

        for (WishlistResponse response : responses) {
            totalPrice += response.getEbookPrice();
            totalDiscountPrice += response.getDiscountPrice();
            totalSalePrice += response.getSalePrice();
        }
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalDiscountPrice", totalDiscountPrice);
        model.addAttribute("totalSalePrice", totalSalePrice);
        model.addAttribute("responses", responses);
        return "wishlist/wishlist";
    }
}
