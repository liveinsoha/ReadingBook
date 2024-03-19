package com.example.demo.web.service;

import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.dto.request.*;
import com.example.demo.web.dto.response.BookInformationResponse;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class BookInformationServiceTest {

    @Autowired
    InitClass initClass;

    @Autowired
    BookInformationService bookInformationService;


    String isbn = "123123";

    @BeforeEach
    void before() {
        initClass.initBookAndAuthorData();
    }

    @Test
    void getBookInformationTest() {
        BookInformationResponse bookInformation = bookInformationService.getBookInformation(isbn);
        System.out.println("bookInformation = " + bookInformation);
    }



}