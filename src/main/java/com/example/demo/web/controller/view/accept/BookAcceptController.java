package com.example.demo.web.controller.view.accept;


import com.example.demo.web.service.BookAcceptService;
import com.example.demo.web.service.BookManageService;
import com.example.demo.web.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookAcceptController {

    private final BookAcceptService bookAcceptService;
}
