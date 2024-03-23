package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Category;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.service.InitClass;
import com.example.demo.web.service.ReviewService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ReviewRepositoryTest {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    InitClass initClass;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    void test() {
        initClass.initMemberDataSmall(); //5명 멤버 등록
        initClass.initCategoryData();
        for (int i = 1; i <= 5; i++) { //책 5개 등록
            initClass.initBookData(i);
        }
        //initClass.initOrderData();

        entityManager.flush();
        entityManager.clear();

        Category category = categoryRepository.getReferenceById(1L);

        bookRepository.deleteByCategory(category);
        categoryRepository.deleteById(1L);

        assertThat(bookRepository.findByCategory(category).size()).isEqualTo(0);

      /*  Member member = initClass.getMember(1L);


        for (long i = 1; i <= 5; i++) { //책 5개에 각각 리뷰 작성
            Book book = initClass.getBook(i);
            Long savedId1 = reviewService.review(member, book, "reviewContent", 5);
        }

        //리뷰 5개 등록 확인
        assertThat(reviewRepository.findAllByMember(member).size()).isEqualTo(5);


        //deleteByMember 쿼리 확인.
        reviewRepository.deleteByMember(member);

        //삭제 결과 확인
        assertThat(reviewRepository.findAllByMember(member).size()).isEqualTo(0);*/
    }
}