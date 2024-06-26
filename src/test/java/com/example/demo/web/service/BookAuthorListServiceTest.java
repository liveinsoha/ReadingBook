package com.example.demo.web.service;

import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.dto.request.*;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BookAuthorListServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    private BookAuthorListService bookAuthorListService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookManageService bookManagementService;

    @Autowired
    private AuthorManageService authorManagementService;

    @Autowired
    private CategoryGroupService categoryGroupService;

    @Test
    void whenAuthorDuplicate_thenThrowException() {
        //given
        Long bookId = createBook();
        Long authorId = createAuthor("J.K 롤링", AuthorOption.AUTHOR);

        //when
        BookAuthorListRegisterRequest request1 = new BookAuthorListRegisterRequest(bookId, authorId, 1);
        BookAuthorListRegisterRequest request2 = new BookAuthorListRegisterRequest(bookId, authorId, 2);
        bookAuthorListService.register(request1);
        assertThatThrownBy(() -> bookAuthorListService.register(request2))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.BOOK_AUTHOR_ALREADY_EXIST.getMessage());
    }

    @Test
    void whenAuthorOrdinalDuplicate_thenThrowException() {
        Long bookId = createBook();
        //given
        Long authorId1 = createAuthor("J.K 롤링", AuthorOption.AUTHOR);
        Long authorId2 = createAuthor("J.K 롤링파스타", AuthorOption.AUTHOR);

        //when
        BookAuthorListRegisterRequest request1 = new BookAuthorListRegisterRequest(bookId, authorId1, 1);
        BookAuthorListRegisterRequest request2 = new BookAuthorListRegisterRequest(bookId, authorId2, 1);
        bookAuthorListService.register(request1);
        assertThatThrownBy(() -> bookAuthorListService.register(request2))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.BOOK_AUTHOR_ORDINAL_ALREADY_EXIST.getMessage());
    }

    @Test
    void whenBookIdNull_ThenThrowException() {
        //given
        Long authorId = createAuthor("J.K 롤링", AuthorOption.AUTHOR);

        //when
        BookAuthorListRegisterRequest request = new BookAuthorListRegisterRequest(null, authorId, 1);
        assertThatThrownBy(() -> bookAuthorListService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("도서 아이디를 입력해주세요.");
    }

    @Test
    void whenAuthorIdNull_ThenThrowException() {
        //given
        Long bookId = createBook();

        BookAuthorListRegisterRequest request = new BookAuthorListRegisterRequest(bookId, null, 1);
        assertThatThrownBy(() -> bookAuthorListService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("작가 아이디를 입력해주세요.");
    }

    @Test
    void whenAuthorRegistered_thenVerifyRegistered() {
        Long bookId = createBook();
        Long authorId = createAuthor("J.K 롤링", AuthorOption.AUTHOR);
        Long translatorId = createAuthor("유재석", AuthorOption.TRANSLATOR);
        Long illustratorId = createAuthor("박명수", AuthorOption.ILLUSTRATOR);

        BookAuthorListRegisterRequest authorRequest = new BookAuthorListRegisterRequest(bookId, authorId, 1);
        BookAuthorListRegisterRequest translatorRequest = new BookAuthorListRegisterRequest(bookId, translatorId, 2);
        BookAuthorListRegisterRequest illustratorRequest = new BookAuthorListRegisterRequest(bookId, illustratorId, 3);

        bookAuthorListService.register(authorRequest);
        bookAuthorListService.register(translatorRequest);
        bookAuthorListService.register(illustratorRequest);
    }

    private static MockMultipartFile getMockMultipartFile() {
        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );
        return file;
    }


    private Long createAuthor(String name, AuthorOption option) {
        AuthorRegisterRequest authorRequest = createAuthorRegisterRequest(name, option, "테스트", "테스트입니다", "1919", Gender.SECRET);
        Long authorId = authorManagementService.registerAuthor(authorRequest);
        return authorId;
    }

    @Test
    void whenAuthorDeleted_thenVerifyIsDeleted() {
        Long bookId = createBook();
        Long authorId = createAuthor("J.K 롤링", AuthorOption.AUTHOR);

        BookAuthorListRegisterRequest request = new BookAuthorListRegisterRequest(bookId, authorId, 1);
        bookAuthorListService.register(request);
        bookAuthorListService.delete(bookId, authorId);

        assertThatThrownBy(() -> bookAuthorListService.findBookAuthorListByBookIdAndAuthorId(bookId, authorId))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.BOOK_AUTHOR_LIST_NOT_FOUND.getMessage());
    }


    private Long createBook() {
        MemberRegisterRequest memberRegisterRequest1 = createMemberRegisterRequest("test@example.com", "test1234", "test1234", "test1", "1999", Gender.SECRET, "01012341234");
        Long memberId1 = memberService.register(memberRegisterRequest1).getMemberId();

        BookRegisterRequest bookRegisterRequest = createBookRegisterRequest("해리포터와 마법사의", "4141", "포터모어",
                "2023.01.01", 0, 9900, 5, 1L, 1L, "21세기 최고의 책");

        MockMultipartFile file = getMockMultipartFile();

        return bookManagementService.registerBook(bookRegisterRequest, file, memberId1);
    }


    private static BookRegisterRequest createBookRegisterRequest(String title, String isbn, String publisher, String publishingDate,
                                                                 int paperPrice, int ebookPrice, int discountRate, Long categoryId, Long bookGroupId, String description) {
        return new BookRegisterRequest(title, isbn, publisher, publishingDate, paperPrice, ebookPrice, discountRate, categoryId, bookGroupId, description);
    }

    private AuthorRegisterRequest createAuthorRegisterRequest(String name, AuthorOption option, String nationality, String description, String birthYear, Gender gender) {
        return new AuthorRegisterRequest(name, option, nationality, description, birthYear, gender);
    }

    private MemberRegisterRequest createMemberRegisterRequest(String email, String password, String
            passwordConfirm, String name, String birthYear, Gender gender, String phoneNo) {
        return new MemberRegisterRequest(email, password, passwordConfirm, name, birthYear, gender, phoneNo);
    }
}