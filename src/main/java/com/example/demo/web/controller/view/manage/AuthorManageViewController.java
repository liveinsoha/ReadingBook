package com.example.demo.web.controller.view.manage;

import com.example.demo.web.domain.entity.Author;
import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.dto.response.AuthorInformationResponse;
import com.example.demo.web.dto.response.AuthorSearchResponse;
import com.example.demo.web.service.AuthorManageService;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthorManageViewController {

    private final AuthorManageService authorManageService;

    @GetMapping("/register/author")
    public String registerForm(Model model) {
        model.addAttribute("selectFlag", "registerAuthor");
        return "manage/author/author-register";
    }

    @GetMapping("/update/author/{authorId}")
    public String updateForm(Model model, @PathVariable Long authorId) {
        Author author = authorManageService.findAuthorById(authorId);
        AuthorInformationResponse authorInformationResponse = new AuthorInformationResponse(author);
        model.addAttribute("author", authorInformationResponse); // 작가 정보를 모델에 추가
        model.addAttribute("selectFlag", "updateAuthor");
        return "manage/author/author-update";
    }


    @GetMapping("/search/author")
    public String searchForm(@RequestParam(required = false, defaultValue = "") String query,
                             Model model) {
        List<AuthorSearchResponse> responses ;
        if (query.isEmpty()) {
            responses = authorManageService.searchAllAuthors();
        } else {
            responses = authorManageService.searchByAuthorName(query);
        }
        model.addAttribute("responses", responses);
        model.addAttribute("query", query);
        model.addAttribute("selectFlag", "searchAuthor");
        return "manage/author/author-search";
    }


    @GetMapping("/result/author")
    public ResponseEntity<BaseResponse<List<AuthorSearchResponse>>> returnSearchResult(@RequestParam String name) {
        List<AuthorSearchResponse> authorSearchResponses = authorManageService.searchByAuthorName(name);
        BaseResponse<List<AuthorSearchResponse>> response = new BaseResponse<>(HttpStatus.OK, "검색 성공적으로 성공하였습니다", authorSearchResponses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/delete/author")
    public String deleteForm(Model model) {
        model.addAttribute("selectFlag", "deleteAuthor");
        return "manage/author/author-delete";
    }
}