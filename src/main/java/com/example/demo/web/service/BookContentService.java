package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.BookContent;
import com.example.demo.web.dto.request.BookContentRegisterRequest;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.BookContentRepository;
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
    private final BookService bookService;

    /**
     * 도서 내용 등록 메소드
     * 이미 등록되어 있을 경우 예외 발생
     * @param request
     * @return bookContentId
     */
    public Long register(BookContentRegisterRequest request) {
        Long bookId = request.getBookId();
        String content = request.getContent();

        boolean isRegisteredBookId = bookContentRepository.existsByBookId(bookId);
        if(isRegisteredBookId == true){
            throw new BaseException(BaseResponseCode.BOOK_CONTENT_ALREADY_REGISTERED);
        }

        validateForm(bookId, content);



        Book book = bookService.findBook(bookId);
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

    /**
     * 도서 내용 수정 메소드
     * @param bookId
     * @param content
     */
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

    /**
     * 도서 내용 제거
     * @param bookId
     */
    public void delete(Long bookId) {
        validateId(bookId);
        BookContent bookContent = findBookContentByBookId(bookId);
        bookContentRepository.delete(bookContent);
    }
}