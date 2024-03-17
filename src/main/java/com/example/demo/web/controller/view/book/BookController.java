package com.example.demo.web.controller.view.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    @GetMapping("/book/{isbn}")
    public String book(@PathVariable String isbn){
        return "book/book";
    }
}