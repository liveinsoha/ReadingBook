package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Wishlist;
import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.dto.request.*;
import com.example.demo.web.exception.BaseException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class WishlistServiceTest {

    @Autowired
    TestInitClass TestInitClass;

    @Autowired
    WishlistService wishlistService;

    @BeforeEach
    void beforeEach() {
//        TestInitClass.initBookAndAuthorData(); //멤버 1명 초기 데이터
//        TestInitClass.initMemberData(); //책 2권 초기 데이터
    }

    @Test
    void wishlist_save_success_test() {
        TestInitClass.initBookAndAuthorData(); //멤버 1명 초기 데이터
        TestInitClass.initMemberData(); //책 2권 초기 데이터

        Book book1 = TestInitClass.getBook(1L);
        Member member = TestInitClass.getMember(1L);

        Long savedId = wishlistService.addBook(member, book1);

        Wishlist wishlist = wishlistService.findWishlist(savedId);

        assertThat(wishlist.getBook()).isEqualTo(book1);
        assertThat(wishlist.getMember()).isEqualTo(member);
    }

    @Test
    void when_purchasedBookExist_then_throwError(){
        TestInitClass.initBookAndAuthorData(); //멤버 1명 초기 데이터
        TestInitClass.initMemberData(); //책 2권 초기 데이터
        TestInitClass.initOrderData(); //주문 2건 초기 데이터


        Book book1 = TestInitClass.getBook(1L);
        Member member = TestInitClass.getMember(1L);

        assertThatThrownBy(() -> wishlistService.addBook(member, book1))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("이미 구매한 도서입니다.");
    }

    @Test
    void when_alreadyWishlistExist_then_throwsError(){
        TestInitClass.initBookAndAuthorData(); //멤버 1명 초기 데이터
        TestInitClass.initMemberData(); //책 2권 초기 데이터

        Book book1 = TestInitClass.getBook(1L);
        Member member = TestInitClass.getMember(1L);

        Long savedId = wishlistService.addBook(member, book1);
        assertThatThrownBy(() -> wishlistService.addBook(member, book1))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("위시리스트에 이미 해당 도서가 존재합니다.");
    }


}