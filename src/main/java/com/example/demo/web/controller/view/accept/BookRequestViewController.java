package com.example.demo.web.controller.view.accept;


import com.example.demo.web.dto.response.RequestedBookDto;
import com.example.demo.web.dto.response.paging.PagingRequestBookDto;
import com.example.demo.web.service.BookRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookRequestViewController {

    private final BookRequestService bookAcceptService;

    @GetMapping("/vendor/request")
    public String requestedBookView(Model model,
                                    @PageableDefault(size = 5) Pageable pageable) {

        Page<RequestedBookDto> requestedBooks = bookAcceptService.getRequestedBooks(pageable);
        PagingRequestBookDto paging = new PagingRequestBookDto(requestedBooks);

        model.addAttribute("responses", requestedBooks);
        model.addAttribute("paging", paging);
        model.addAttribute("selectFlag", "requestedBook");
        return "vendor/request";
    }
}
