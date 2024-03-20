package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Book findBook(Long bookId){
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOK_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Book findBook(String isbn){
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOK_NOT_FOUND));
    }


    public List<Book> findAllById(List<Long> bookIdList) {
        if(bookIdList.size() == 0){
            throw new IllegalArgumentException("도서가 없습니다.");
        }
        return bookRepository.findAllById(bookIdList);
    }
}
