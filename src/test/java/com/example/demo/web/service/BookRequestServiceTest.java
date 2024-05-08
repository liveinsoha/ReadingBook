package com.example.demo.web.service;

import com.example.demo.web.dto.response.RequestedBookDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookRequestServiceTest {

    @Autowired
    BookRequestService bookRequestService;

    @Test
    void QueryTest() {
        Page<RequestedBookDto> requestedBooks = bookRequestService.getRequestedBooks(PageRequest.of(0, 5));
        System.out.println("requestedBooks = " + requestedBooks);
    }
}