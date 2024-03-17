package com.example.demo.web.controller.view.manage;

import com.example.demo.web.dto.response.AuthorSearchResponse;
import com.example.demo.web.service.AuthorManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage/author")
public class AuthorManageViewController {

    private final AuthorManageService authorManageService;

    @GetMapping("/register/author")
    public String registerForm(Model model){
        model.addAttribute("selectFlag", "registerAuthor");
        return "manage/author/author-register";
    }

    @GetMapping("/update/author")
    public String updateForm(Model model){
        model.addAttribute("selectFlag", "updateAuthor");
        return "manage/author/author-update";
    }

    @GetMapping("/search/author")
    public String searchForm(Model model){
        model.addAttribute("selectFlag", "searchAuthor");
        return "manage/author/author-search";
    }

    @GetMapping("/result/author")
    public String returnSearchResult(@RequestParam String name, Model model){
        List<AuthorSearchResponse> response = authorManageService.searchByAuthorName(name);

        model.addAttribute("responses", response);
        model.addAttribute("search", name);
        model.addAttribute("selectFlag", "searchAuthor");
        return "manage/author/author-result";
    }

    @GetMapping("/delete/author")
    public String deleteForm(Model model){
        model.addAttribute("selectFlag", "deleteAuthor");
        return "manage/author/author-delete";
    }
}