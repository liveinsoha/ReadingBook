package com.example.demo.web.service;

import com.example.demo.web.dto.response.HomeBooksResponse;
import com.example.demo.web.exception.BaseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(noRollbackFor = {BaseException.class})
@Rollback(value = false)
class HomeServiceTest {

    @Autowired
    InitClass initClass;

    @Autowired
    HomeService homeService;

    @Test
    void findBestBooksTest() {
        initClass.initOrderAndReviewBigData();

        List<HomeBooksResponse> bestBooks = homeService.findBestBooks();
        System.out.println("bestBooks = " + bestBooks);
    }

    @Test
    void findNewBooksTest() {
    }
}