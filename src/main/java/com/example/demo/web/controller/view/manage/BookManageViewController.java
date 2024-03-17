package com.example.demo.web.controller.view.manage;

import com.example.demo.web.dto.response.BookUpdateResponse;
import com.example.demo.web.service.BookManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookManageViewController {

     private final BookManageService bookManageService;
    @GetMapping("/register/book")
    public String registerForm(Model model){
        model.addAttribute("selectFlag", "registerBook");
        return "manage/book/book-register";
    }


    @GetMapping("/update/book")
    public String updateForm(Model model, Long bookId){
        BookUpdateResponse response = bookManageService.searchUpdateBook(bookId);

        if(response == null){
            model.addAttribute("isSearched", false);
        } else{
            model.addAttribute("isSearched", true);
        }

        model.addAttribute("response", response);
        return "manage/book/book-update";
    }




    @GetMapping("/update/search/book")
    public String updateSearchForm(Model model){
        model.addAttribute("selectFlag", "updateBook");
        return "manage/book/book-update-search";
    }
}