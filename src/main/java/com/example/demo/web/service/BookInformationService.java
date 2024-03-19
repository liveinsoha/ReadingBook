package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.BookAuthorList;
import com.example.demo.web.domain.entity.BookGroup;
import com.example.demo.web.dto.response.*;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.BookAuthorListRepository;
import com.example.demo.web.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookInformationService {

    private final BookRepository bookRepository;
    private final BookAuthorListRepository bookAuthorListRepository;

    public BookInformationResponse getBookInformation(String isbn) {
        Optional<Book> tempBook = bookRepository.getBookInformation(isbn);

        if(tempBook.isEmpty()){ // 해당 하는 책이 없을 경우 null 리턴.
            return null;
        }

        BookDto bookDto = tempBook //Book으로부터 BookDto를 만듦
                .map(b -> new BookDto(b)).get();

        BookAuthorList authorInBook = bookAuthorListRepository.getMainAuthor(isbn);
        String author = authorInBook.getAuthor().getName();
        Long authorId = authorInBook.getAuthor().getId();

        int authorCountExceptMainAuthor = bookAuthorListRepository.getAuthorCount(isbn).intValue() - 1;

        BookAuthorList translatorInBook = bookAuthorListRepository.getMainTranslator(isbn);

        String translator = null;
        Long translatorId = null;
        if(translatorInBook != null){ //여기 확인 필요.
            translator = translatorInBook.getAuthor().getName();
            translatorId = translatorInBook.getAuthor().getId();
        }

        AuthorDto authorDto = new AuthorDto(authorId, author, translatorId, authorCountExceptMainAuthor, translator);

        BookInformationResponse response = new BookInformationResponse(bookDto, authorDto);
        return response;
    }

    /**
     * 이 책이 속해있는 도서그룹을 찾고, 도서그룹 Id로 같은 시리즈에 속해있는 책 정보들을 얻는다. 없을 경우 빈 리스트 리턴
     * @param isbn
     * @return
     */
    public List<BookGroupInformationResponse> getSeriesInformation(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        Optional<BookGroup> bookGroup = Optional.ofNullable(book.get().getBookGroup()); //지연로딩 발생지점.

        if(bookGroup.isEmpty()){
            return new ArrayList<>();
        }

        return bookRepository.findByBookGroupId(bookGroup.get().getId()).stream()
                .map(b -> new BookGroupInformationResponse(b.getIsbn(), b.getTitle(), b.getSavedImageName()))
                .collect(Collectors.toList());
    }

    /**
     * 책 isbn에 해당하는 모든 작가들 이름과 작가번호 얻는다
     * @param isbn
     * @return
     */
    public List<AuthorNameAndIdResponse> getAuthorNameAndIdList(String isbn) {
        return bookAuthorListRepository.getAuthorNameAndIdList(isbn).stream()
                .map(bal -> new AuthorNameAndIdResponse(bal.getAuthor().getName(), bal.getAuthor().getId(), bal.getAuthor().getAuthorOption().toString()))
                .collect(Collectors.toList());
    }

    public AuthorInformationResponse getAuthorInformation(String isbn, Long authorId) {
        return bookAuthorListRepository.getAuthorInformation(isbn, authorId)
                .map(bal -> new AuthorInformationResponse(bal.getAuthor()))
                .orElseThrow(() -> new BaseException(BaseResponseCode.AUTHOR_NOT_FOUND));
    }
}
