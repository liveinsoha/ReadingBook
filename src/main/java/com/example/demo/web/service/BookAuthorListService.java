package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Author;
import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.BookAuthorList;
import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.dto.request.BookAuthorListRegisterRequest;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.BookAuthorListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookAuthorListService {
    private final BookAuthorListRepository bookAuthorListRepository;
    private final AuthorManageService authorManagementService;
    private final BookManageService bookManagementService;

    public Long register(BookAuthorListRegisterRequest request) {
        Long bookId = request.getBookId();
        Long authorId = request.getAuthorId();
        int ordinal = request.getOrdinal();

        validateForm(bookId, authorId, ordinal);

        Book book = bookManagementService.findBookById(bookId);
        Author author = authorManagementService.findAuthorById(authorId);

        BookAuthorList bookAuthorList = BookAuthorList.createBookAuthorList(book, author, ordinal);

        return bookAuthorListRepository.save(bookAuthorList).getId();
    }

    private static void validateForm(Long bookId, Long authorId, int ordinal) {
        validateId(bookId, authorId);

        if(ordinal == 0){
            throw new IllegalArgumentException("서수를 입력해주세요.");
        }

    }

    private static void validateId(Long bookId, Long authorId) {
        if(bookId == null){
            throw new IllegalArgumentException("도서 아이디를 입력해주세요.");
        }

        if(authorId == null){
            throw new IllegalArgumentException("작가 아이디를 입력해주세요.");
        }
    }

    public void delete(Long bookId, Long authorId) {
        validateId(bookId, authorId);

        BookAuthorList bookAuthorList = findBookAuthorListByBookIdAndAuthorId(bookId, authorId);

        bookAuthorListRepository.delete(bookAuthorList);
    }

    @Transactional(readOnly = true)
    public BookAuthorList findBookAuthorListByBookIdAndAuthorId(Long bookId, Long authorId) {
        BookAuthorList bookAuthorList = bookAuthorListRepository.findByBookIdAndAuthorId(bookId, authorId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOK_AUTHOR_LIST_NOT_FOUND));
        return bookAuthorList;
    }
}