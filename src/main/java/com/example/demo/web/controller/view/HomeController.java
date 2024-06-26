package com.example.demo.web.controller.view;

import com.example.demo.web.dto.response.HomeBooksResponse;
import com.example.demo.web.service.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final HomeService homeService;

    @RequestMapping("/")
    public String home(Model model) {
        List<HomeBooksResponse> bestBooks = homeService.findBestBooks();
        model.addAttribute("bestBooks", bestBooks);

        List<HomeBooksResponse> newBooks = homeService.findNewBooks();
        model.addAttribute("newBooks", newBooks);
        return "home/home";
    }
}