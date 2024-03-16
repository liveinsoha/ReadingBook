package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Author;
import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.dto.request.AuthorRegisterRequest;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthorManageServiceTest {

    @Autowired
    AuthorManageService authorManageService;

    @Test
    void register_fail_empty_name() {
        AuthorRegisterRequest request = createRequest("", AuthorOption.AUTHOR, "Korea", "Description", "1990", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.EMPTY_NAME.getMessage());
    }

    @Test
    void register_fail_invalid_name_length() {
        AuthorRegisterRequest request = createRequest("A", AuthorOption.AUTHOR, "Korea", "Description", "1990", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.INVALID_NAME_LENGTH.getMessage());
    }

    @Test
    void register_fail_empty_option() {
        AuthorRegisterRequest request = createRequest("John Doe", null, "Korea", "Description", "1990", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.EMPTY_OPTION.getMessage());
    }

    @Test
    void register_fail_empty_nationality() {
        AuthorRegisterRequest request = createRequest("John Doe", AuthorOption.AUTHOR, "", "Description", "1990", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.EMPTY_NATIONALITY.getMessage());
    }

    @Test
    void register_fail_empty_description() {
        AuthorRegisterRequest request = createRequest("John Doe", AuthorOption.AUTHOR, "Korea", "", "1990", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.EMPTY_DESCRIPTION.getMessage());
    }

    @Test
    void register_fail_empty_birth_year() {
        AuthorRegisterRequest request = createRequest("John Doe", AuthorOption.AUTHOR, "Korea", "Description", "", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.EMPTY_BIRTH_YEAR.getMessage());
    }

    @Test
    void register_fail_invalid_birth_year_format() {
        AuthorRegisterRequest request = createRequest("John Doe", AuthorOption.AUTHOR, "Korea", "Description", "19901", Gender.MEN);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.INVALID_BIRTH_YEAR_FORMAT.getMessage());
    }

    @Test
    void register_fail_empty_gender() {
        AuthorRegisterRequest request = createRequest("John Doe", AuthorOption.AUTHOR, "Korea", "Description", "1990", null);
        assertThatThrownBy(() -> authorManageService.registerAuthor(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(BaseResponseCode.EMPTY_GENDER.getMessage());
    }

    // Helper method to create AuthorRegisterRequest
    private AuthorRegisterRequest createRequest(String name, AuthorOption option, String nationality, String description, String birthYear, Gender gender) {
        AuthorRegisterRequest authorRegisterRequest = new AuthorRegisterRequest(name, option, nationality, description, birthYear, gender);
        return authorRegisterRequest;
    }
}