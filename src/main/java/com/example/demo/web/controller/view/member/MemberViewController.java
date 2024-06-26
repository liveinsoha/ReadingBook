package com.example.demo.web.controller.view.member;

import com.example.demo.web.dto.response.MemberInformationResponse;
import com.example.demo.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class MemberViewController {

    private final MemberService memberService;

    @GetMapping("/home")
    public String home(Principal principal, Model model) {
        MemberInformationResponse response = memberService.findMemberInformation(principal);
        model.addAttribute("response", response);
        return "user/home";
    }

    @GetMapping("/modify")
    public String modifyForm(Model model) {
        model.addAttribute("active", "modifyAccount");
        return "user/modify/modify";
    }
}