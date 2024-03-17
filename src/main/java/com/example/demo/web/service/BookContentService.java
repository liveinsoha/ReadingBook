package com.example.demo.web.service;

import com.example.demo.web.controller.api.controller.BookContentRepository;
import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.BookContent;
import com.example.demo.web.dto.request.BookContentRegisterRequest;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookContentService {

    private final BookContentRepository bookContentRepository;
    private final BookManageService bookManagementService;

    public Long register(BookContentRegisterRequest request) {
        Long bookId = request.getBookId();
        String content = request.getContent();

        validateForm(bookId, content);

        Book book = bookManagementService.findBookById(bookId);
        BookContent bookContent = BookContent.createBookContent(book, content);

        return bookContentRepository.save(bookContent).getId();
    }

    private static void validateForm(Long bookId, String content) {
        validateId(bookId);

        if(content == null || content.trim().equals("")){
            throw new IllegalArgumentException("도서의 내용을 입력해주세요.");
        }
    }

    private static void validateId(Long bookId) {
        if(bookId == null){
            throw new IllegalArgumentException("도서 아이디를 입력해주세요.");
        }
    }

    public void update(Long bookId, String content) {
        validateForm(bookId, content);

        BookContent bookContent = findBookContentByBookId(bookId);
        bookContent.updateContent(content);
    }

    @Transactional(readOnly = true)
    public BookContent findBookContentByBookId(Long bookId) {
        return bookContentRepository.findByBookId(bookId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOK_NOT_FOUND));
    }

    public void delete(Long bookId) {
        validateId(bookId);
        BookContent bookContent = findBookContentByBookId(bookId);
        bookContentRepository.delete(bookContent);
    }
}