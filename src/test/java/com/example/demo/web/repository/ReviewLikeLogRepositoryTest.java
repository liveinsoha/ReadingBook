package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Review;
import com.example.demo.web.service.InitClass;
import com.example.demo.web.service.ReviewService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewLikeLogRepositoryTest {


    @Autowired
    ReviewLikeLogRepository reviewLikeLogRepository;

    @Autowired
    InitClass initClass;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    ReviewService reviewService;

    @Test
    void test() {
        initClass.initMemberDataSmall();
        initClass.initCategoryData();
        for (int i = 1; i <= 5; i++) { //책 5개 등록
            initClass.initBookData(i);
        }
        initClass.initOrderData();

        Member member = initClass.getMember(1L);


        for (long i = 1; i <= 5; i++) { //책 5개에 각각 리뷰 작성
            Book book = initClass.getBook(i);
            Long savedId1 = reviewService.review(member, book, "reviewContent", 5);
        }

        entityManager.flush();
        entityManager.clear();

        List<Review> reviews = reviewRepository.findByMember(member);

        reviewLikeLogRepository.mDeleteAllByReviewsInQuery(reviews);
    }

}