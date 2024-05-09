package com.example.demo.web.controller.view.manage;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.dto.response.BookManageSearchResponse;
import com.example.demo.web.dto.response.BookUpdateResponse;
import com.example.demo.web.dto.response.paging.PagingManageBookSearchDto;

import com.example.demo.web.service.BookManageService;
import com.example.demo.web.service.MemberService;
import com.example.demo.web.service.search.BookSearchCondition;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookManageViewController {

    private final BookManageService bookManageService;
    private final MemberService memberService;

    @GetMapping("/register/book")
    public String registerForm(Model model) {
        model.addAttribute("selectFlag", "registerBook");
        return "manage/book/book-register";
    }


    @GetMapping("/update/book/{bookId}")
    public String updateForm(Model model, @PathVariable Long bookId, Principal principal) {
        Member seller = memberService.getMember(principal);
        BookUpdateResponse response = bookManageService.getBook(bookId, seller.getId());

        if (response == null) {
            model.addAttribute("isSearched", false);
        } else {
            model.addAttribute("isSearched", true);
        }

        model.addAttribute("response", response);
        return "manage/book/book-update";
    }


  /*  @GetMapping("/update/search/book")
    public String updateSearchForm(Model model) {
        model.addAttribute("selectFlag", "updateBook");
        return "manage/book/book-update-search";
    }*/

   /* @GetMapping("/result/book")
    public String searchForm(Model model) {

        model.addAttribute("selectFlag", "searchBook");
        model.addAttribute("selectFlag", "searchBook");
        return "manage/book/book-search";
    }
*/

    @GetMapping("/manage/search/book")
    public String returnSearchResult(@PageableDefault(size = 5) Pageable pageable,
                                     @RequestParam(required = false, defaultValue = "") String query,
                                     @ModelAttribute BookSearchCondition condition,
                                     Principal principal,
                                     Model model) {
        Member seller = memberService.getMember(principal);
        Page<BookManageSearchResponse> responses = null;
        if (query.isEmpty()) {
            responses = bookManageService.getAllBooks(pageable, seller);
        } else {
            responses = bookManageService.searchBook(query, pageable, condition, seller);
        }
        PagingManageBookSearchDto paging = new PagingManageBookSearchDto(responses);

        model.addAttribute("responses", responses);
        model.addAttribute("query", query);
        model.addAttribute("paging", paging);
        model.addAttribute("condition", condition);
        return "manage/book/book-search";
    }

    /*@Deprecated
    @GetMapping("/manage/search/book/result")
    public String returnSearchResult(@PageableDefault(size = 5) Pageable pageable,
                                     @RequestParam(required = false) String query,
                                     @ModelAttribute BookSearchCondition condition) {

        Page<BookManageSearchResponse> responses = bookManageService.searchBook(query, pageable, condition);
        PagingManageBookSearchDto paging = new PagingManageBookSearchDto(responses);
        return "search/book-search";
    }*/

    @GetMapping("/manage/book/search-query")
    public ResponseEntity<BaseResponse<List<BookManageSearchResponse>>> returnSearchQuery(@RequestParam(required = false) String query,
                                                                                          Principal principal) {
        Member seller = memberService.getMember(principal);
        List<BookManageSearchResponse> searchBookQuery = bookManageService.searchBookQuery(query, seller);
        BaseResponse<List<BookManageSearchResponse>> response = new BaseResponse<>(HttpStatus.OK, "검색어 추천합니다", searchBookQuery);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/delete/book")
    public String deleteForm(Model model) {
        model.addAttribute("selectFlag", "deleteBook");
        return "manage/bookgroup/book-delete";
    }

}