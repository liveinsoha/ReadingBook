package com.example.demo.web.controller.view.manage;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookContentViewController {

    private final BookService bookService;

    @GetMapping("/register/book-content/{bookId}")
    public String registerForm(Model model, @PathVariable Long bookId) {
        Book book = bookService.findBook(bookId);
        model.addAttribute("book", book);
        model.addAttribute("selectFlag", "registerBookContent");
        return "manage/bookcontent/bookcontent-register";
    }

    @GetMapping("/update/book-content")
    public String updateForm(Model model) {

        model.addAttribute("selectFlag", "updateBookContent");
        return "manage/bookcontent/bookcontent-update";
    }

    @GetMapping("/delete/book-content")
    public String deleteForm(Model model) {
        model.addAttribute("selectFlag", "deleteBookContent");
        return "manage/bookcontent/bookcontent-delete";
    }
}