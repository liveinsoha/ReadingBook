package com.example.demo.web.controller.view.book;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.response.*;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.service.BookInformationService;
import com.example.demo.web.service.MemberService;
import com.example.demo.web.service.ReviewService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookInformationService bookInformationService;
    private final MemberService memberService;
    private final ReviewService reviewService;

    /* --- 책에 대한 상세 정보를 리턴 ---*/
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String book(@PathVariable String isbn, HttpServletResponse response, Principal principal, Model model) throws IOException {
        BookInformationResponse bookInformation = bookInformationService.getBookInformation(isbn);

        if (bookInformation == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return "/error/404";
        }
        /* --- 시리즈 정보 --- */
        List<BookGroupInformationResponse> seriesInformation = bookInformationService.getSeriesInformation(isbn);

        /* --- 작가의 이름과 아이디 --- */
        List<AuthorNameAndIdResponse> authorNameAndIdList = bookInformationService.getAuthorNameAndIdList(isbn);

        /* --- 작가, 번역가, 삽화가의 국적, 소개와 같은 정보들 --- */
        AuthorInformationResponse authorInformation = null;
        Long authorId = bookInformation.getAuthorDto().getAuthorId();

        /* --- 만약 작가 아이디를 조작한 뒤 요청했다면, ControllerAdvice로 예외 처리를 하지 않기 위해 null로 반환 --- */
        try {
            authorInformation = bookInformationService.getAuthorInformation(isbn, authorId);
        } catch (BaseException e) { //작가 정보가 없는경우?
            authorInformation = null;
        }


        /* --- 리뷰에 관련한 정보 --- */
        MyWroteReviewResponse myReview = null;
        try{
            Member member = memberService.getMember(principal);
            Long memberId = member.getId();

            myReview = reviewService.findWroteReview(memberId, isbn);
            model.addAttribute("isLogin", true);
        } catch (BaseException e){
            model.addAttribute("isLogin", false);
        }

        model.addAttribute("myReview", myReview);
        model.addAttribute("information", bookInformation);
        model.addAttribute("booksInGroup", seriesInformation);
        model.addAttribute("authorNameAndIdList", authorNameAndIdList);
        model.addAttribute("authorInformation", authorInformation);
        return "book/book";
    }

    @GetMapping("/book/{isbn}/{authorId}")
    @ResponseBody
    public ResponseEntity<Object> getAuthorInformation(@PathVariable String isbn, @PathVariable Long authorId) {
        AuthorInformationResponse authorInformation = bookInformationService.getAuthorInformation(isbn, authorId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(authorInformation);
    }
}