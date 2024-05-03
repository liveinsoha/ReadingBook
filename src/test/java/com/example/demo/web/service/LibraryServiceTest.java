package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.response.BookContentResponse;
import com.example.demo.web.dto.response.LibraryResponse;
import io.swagger.v3.oas.annotations.media.SchemaProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LibraryServiceTest {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private TestInitClass TestInitClass;

    @Test
    void when_Book_Ordered_then_Check_Library() {
        TestInitClass.initMemberDataSmall();
        TestInitClass.initBookAndAuthorData();
        TestInitClass.initOrderData();

        Member member = TestInitClass.getMember(1L);

        List<LibraryResponse> booksInLibrary = libraryService.findBooksInLibrary(member);

        assertThat(booksInLibrary.size()).isEqualTo(2);
    }

    @Test
    void when_Book_Ordered_then_check_BookContent_In_Library() {
        TestInitClass.initMemberDataSmall();
        TestInitClass.initBookAndAuthorData();
        TestInitClass.initOrderData();

        Member member = TestInitClass.getMember(1L);
        Book book = TestInitClass.getBook(1L);

        BookContentResponse bookContent = libraryService.findBookContent(member, book.getId());
        assertThat(bookContent.getContent()).isEqualTo("testContent1");
        System.out.println("bookContent = " + bookContent);
    }



}