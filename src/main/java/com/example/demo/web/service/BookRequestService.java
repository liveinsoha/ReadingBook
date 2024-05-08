package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.BookContent;
import com.example.demo.web.dto.response.RequestedBookDto;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.BookContentRepository;
import com.example.demo.web.repository.BookRepository;
import com.example.demo.web.repository.BookRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class BookRequestService {

    private final BookRepository bookRepository;
    private final BookContentRepository bookContentRepository;

    public Page<RequestedBookDto> getRequestedBooks(Pageable pageable) {
        Page<RequestedBookDto> requestedBooks = bookRepository.findRequestedBooks(pageable);
        return requestedBooks;
    }

    @Transactional
    public void acceptBookRequest(Long bookId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        book.updateAccepted();
    }

    @Transactional
    public void rejectBookRequest(Long bookId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NO_ID_EXCEPTION));

        book.updateRejected();
    }
}
