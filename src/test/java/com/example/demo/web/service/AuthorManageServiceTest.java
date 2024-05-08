package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Author;
import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.dto.request.*;
import com.example.demo.web.dto.response.AuthorSearchResponse;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthorManageServiceTest {

    @Autowired
    AuthorManageService authorManageService;

    @Autowired
    MemberService memberService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryGroupService categoryGroupService;

    @Autowired
    private BookManageService bookManagementService;

    @Autowired
    private BookAuthorListService authorListService;

    @Test
    void searchAuthorTest() {
        List<AuthorSearchResponse> authors = authorManageService.searchByAuthorName("D");
        System.out.println("authors = " + authors);
    }

    @Test
    void register_fail_empty_name() {
        AuthorRegisterRequest request = createAuthorRegisterRequest("", AuthorOption.AUTHOR, "Korea", "Description", "1990", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.EMPTY_NAME.getMessage());
    }

    @Test
    void register_fail_invalid_name_length() {
        AuthorRegisterRequest request = createAuthorRegisterRequest("A", AuthorOption.AUTHOR, "Korea", "Description", "1990", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.INVALID_NAME_LENGTH.getMessage());
    }

    @Test
    void register_fail_empty_option() {
        AuthorRegisterRequest request = createAuthorRegisterRequest("John Doe", null, "Korea", "Description", "1990", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.EMPTY_OPTION.getMessage());
    }

    @Test
    void register_fail_empty_nationality() {
        AuthorRegisterRequest request = createAuthorRegisterRequest("John Doe", AuthorOption.AUTHOR, "", "Description", "1990", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.EMPTY_NATIONALITY.getMessage());
    }

    @Test
    void register_fail_empty_description() {
        AuthorRegisterRequest request = createAuthorRegisterRequest("John Doe", AuthorOption.AUTHOR, "Korea", "", "1990", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.EMPTY_DESCRIPTION.getMessage());
    }

    @Test
    void register_fail_empty_birth_year() {
        AuthorRegisterRequest request = createAuthorRegisterRequest("John Doe", AuthorOption.AUTHOR, "Korea", "Description", "", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.EMPTY_BIRTH_YEAR.getMessage());
    }

    @Test
    void register_fail_invalid_birth_year_format() {
        AuthorRegisterRequest request = createAuthorRegisterRequest("John Doe", AuthorOption.AUTHOR, "Korea", "Description", "19901", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.INVALID_BIRTH_YEAR_FORMAT.getMessage());
    }

    @Test
    void register_fail_empty_gender() {
        AuthorRegisterRequest request = createAuthorRegisterRequest("John Doe", AuthorOption.AUTHOR, "Korea", "Description", "1990", null);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.EMPTY_GENDER.getMessage());
    }

    @Test
    void whenAuthorSearched_thenVerifyIsSearched() {
        //given
        AuthorRegisterRequest firstRequest = createAuthorRegisterRequest("qwer", AuthorOption.AUTHOR, "영국", "test", "1999", Gender.MEN);
        authorManageService.registerAuthor(firstRequest);
        AuthorRegisterRequest secondRequest = createAuthorRegisterRequest("qwer", AuthorOption.TRANSLATOR, "대한민국", "test", "1999", Gender.MEN);
        authorManageService.registerAuthor(secondRequest);

        //when
        List<AuthorSearchResponse> authors = authorManageService.searchByAuthorName("qwer");

        //given
        assertThat(authors.size()).isEqualTo(2);
        assertThat(authors.get(0).getAuthorOption()).isEqualTo(AuthorOption.AUTHOR.getKorean());
        assertThat(authors.get(1).getAuthorOption()).isEqualTo(AuthorOption.TRANSLATOR.getKorean());
    }

    @Test
    void whenDeletingAuthorExistsBook_thenThrowException() {
        //given
        AuthorRegisterRequest firstRequest = createAuthorRegisterRequest("test", AuthorOption.AUTHOR, "영국", "test", "1999", Gender.MEN);
        Long authorId = authorManageService.registerAuthor(firstRequest);

        Long bookId = registerBook();

        BookAuthorListRegisterRequest authorRequest = new BookAuthorListRegisterRequest(bookId, authorId, 1);
        authorListService.register(authorRequest);

        //when
        assertThatThrownBy(() -> authorManageService.delete(authorId))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("해당 작가에 도서가 등록되었습니다. 하위 도서를 모두 삭제한 다음에 작가을 삭제해주세요.");
    }


    private Long registerBook() {
        MemberRegisterRequest memberRegisterRequest1 = createMemberRegisterRequest("test@example.com", "test1234", "test1234", "test1", "1999", Gender.SECRET, "01012341234");
        Long memberId1 = memberService.register(memberRegisterRequest1).getMemberId();

        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );

        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest);


        BookRegisterRequest request = createBookRegisterRequest("해리포터와 마법사의 돌", "123123", "포터모어",
                "2023.01.01", 0, 9900, 5, categoryId, null, "21세기 최고의 책");

        return bookManagementService.registerBook(request, file, memberId1);
    }


    private static BookRegisterRequest createBookRegisterRequest(String title, String isbn, String publisher, String publishingDate, int paperPrice, int ebookPrice, int discountRate,
                                                                 Long categoryId, Long bookGroupId, String description) {
        return new BookRegisterRequest(title, isbn, publisher, publishingDate, paperPrice, ebookPrice, discountRate, categoryId, bookGroupId, description);
    }

    // Helper method to create AuthorRegisterRequest
    private AuthorRegisterRequest createAuthorRegisterRequest(String name, AuthorOption option, String nationality, String description, String birthYear, Gender gender) {
        AuthorRegisterRequest authorRegisterRequest = new AuthorRegisterRequest(name, option, nationality, description, birthYear, gender);
        return authorRegisterRequest;
    }


    private MemberRegisterRequest createMemberRegisterRequest(String email, String password, String
            passwordConfirm, String name, String birthYear, Gender gender, String phoneNo) {
        return new MemberRegisterRequest(email, password, passwordConfirm, name, birthYear, gender, phoneNo);
    }
}