package com.example.demo.web.controller.view.search;

import com.example.demo.web.dto.response.paging.PagingDto;
import com.example.demo.web.dto.response.BookSearchResponse;
import com.example.demo.web.dto.response.paging.PagingSearchDto;
import com.example.demo.web.service.search.BookSearchCondition;
import com.example.demo.web.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public String search(@PageableDefault(size = 5) Pageable pageable,
                         @RequestParam(required = false) String query,
                         @ModelAttribute BookSearchCondition condition,
                         Model model){
        Page<BookSearchResponse> responses =  searchService.search(query, pageable, condition);

        PagingSearchDto paging = new PagingSearchDto(responses);

        model.addAttribute("responses", responses);
        model.addAttribute("query", query);
        model.addAttribute("paging", paging);
        model.addAttribute("condition", condition);
        return "search/search";
    }

    @GetMapping("/search-test")
    @ResponseBody
    public ResponseEntity<Object> test(@PageableDefault(size = 2) Pageable pageable, String query, BookSearchCondition condition){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(searchService.search(query, pageable, condition));
    }
}