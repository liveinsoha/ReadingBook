package com.example.demo.web.controller.view.manage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage/author")
public class AuthorManageViewController {

    @GetMapping
    public String registerForm(Model model){
        model.addAttribute("selectFlag", "registerAuthor");
        return "manage/author/author-register";
    }
}