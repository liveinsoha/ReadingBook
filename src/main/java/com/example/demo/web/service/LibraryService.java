package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.BookContent;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.response.BookContentResponse;
import com.example.demo.web.dto.response.LibraryResponse;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.BookContentRepository;
import com.example.demo.web.repository.BookRepository;
import com.example.demo.web.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookContentRepository bookContentRepository;
    private final BookRepository bookRepository;

    /**
     * 구매한 도서 조회 메소드
     * @param member
     * @return List<DTO>
     */

    public List<LibraryResponse> findBooksInLibrary(Member member) {
        return libraryRepository.findAllByMember(member).stream()
                .map(LibraryResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 구매한 도서 내용 열람 메소드
     * @param member
     * @param bookId
     * @return
     */
    public BookContentResponse findBookContent(Member member, Long bookId) {
        /* --- 구매한 도서를 열람하는 지 확인 --- */
        Book book = bookRepository.getReferenceById(bookId);

        boolean isBought = libraryRepository.existsByBookAndMember(book, member);
        if(!isBought){
            throw new BaseException(BaseResponseCode.BOOK_PURCHASE_REQUIRED);
        }

        BookContent bookContent = bookContentRepository.findByBook(book);
        return new BookContentResponse(bookContent);
    }
}