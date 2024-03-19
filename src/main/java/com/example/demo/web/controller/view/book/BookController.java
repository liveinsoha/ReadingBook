package com.example.demo.web.controller.view.book;

import com.example.demo.web.dto.response.AuthorInformationResponse;
import com.example.demo.web.dto.response.AuthorNameAndIdResponse;
import com.example.demo.web.dto.response.BookGroupInformationResponse;
import com.example.demo.web.dto.response.BookInformationResponse;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.service.BookInformationService;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookInformationService bookInformationService;

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String book(@PathVariable String isbn, HttpServletResponse response, Model model) throws IOException {
        BookInformationResponse bookInformation = bookInformationService.getBookInformation(isbn);

        if (bookInformation == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return "/error/404";
        }

        List<BookGroupInformationResponse> seriesInformation = bookInformationService.getSeriesInformation(isbn);
        List<AuthorNameAndIdResponse> authorNameAndIdList = bookInformationService.getAuthorNameAndIdList(isbn);

        AuthorInformationResponse authorInformation = null;
        Long authorId = bookInformation.getAuthorDto().getAuthorId();

        try {
            authorInformation = bookInformationService.getAuthorInformation(isbn, authorId);
        } catch (BaseException e) { //작가 정보가 없는경우?
            authorInformation = null;
        }

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