package com.example.demo.web.controller.view.manage;

import com.example.demo.web.domain.entity.BookGroup;
import com.example.demo.web.dto.response.BookGroupSearchResponse;
import com.example.demo.web.service.BookGroupManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookGroupManageViewController {

    private final BookGroupManageService bookGroupManagementService;

    @GetMapping("/register/book-group")
    public String registerForm(Model model) {
        model.addAttribute("selectFlag", "registerBookGroup");
        return "manage/bookgroup/bookgroup-register";
    }

    @GetMapping("/update/book-group/{bookGroupId}")
    public String updateBookGroup(Model model, @PathVariable Long bookGroupId) {
        BookGroup response = bookGroupManagementService.findBookGroupById(bookGroupId);

        if (response == null) {
            model.addAttribute("isSearched", false);
        } else {
            model.addAttribute("isSearched", true);
        }

        model.addAttribute("response", response);
        model.addAttribute("selectFlag", "updateBookGroup");

        return "manage/bookgroup/bookgroup-update";
    }

    @GetMapping("/res/book-group")
    public String searchForm(Model model) {
        model.addAttribute("selectFlag", "searchBookGroup");
        return "manage/bookgroup/bookgroup-search";
    }

    @GetMapping("/search/book-group")
    public String returnSearchResult(@RequestParam(required = false, defaultValue = "") String title,
                                     Model model) {
        List<BookGroupSearchResponse> responses = null;
        if (title.isEmpty()) {
            responses = bookGroupManagementService.searchAllBookGroup();
        } else {
            responses = bookGroupManagementService.searchByBookGroupTitle(title);
        }

        System.out.println("responses = " + responses);

        model.addAttribute("responses", responses);
        model.addAttribute("search", title);
        model.addAttribute("selectFlag", "searchBookGroup");
        return "manage/bookgroup/bookgroup-search";
    }

    @GetMapping("/delete/book-group")
    public String deleteForm(Model model) {
        model.addAttribute("selectFlag", "deleteBookGroup");
        return "manage/bookgroup/bookgroup-delete";
    }


}