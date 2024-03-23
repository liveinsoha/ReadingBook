package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.request.MemberRegisterRequest;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.exception.BaseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void register_fail_null() {
        MemberRegisterRequest request = createRequest(null, null, null, null, null, null,"01012341234");
        assertThatThrownBy(
                () -> memberService.register(request))
                .isInstanceOf(BaseException.class);
    }

    @Test
    void register_fail_invalid_email() {
        MemberRegisterRequest request = createRequest("tes@example.com", "test1234", "test1234", "test", "1999", Gender.SECRET,"01012341234");
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("이메일을 올바르게 입력해주세요.");

    }

    @Test
    void register_fail_invalid_short_password() {
        MemberRegisterRequest request = createRequest("test@example.com", "test", "test", "test", "1999", Gender.SECRET,"01012341234");
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("비밀번호를 올바르게 입력해주세요. 비밀번호는 8-16자에 특수문자 '@, $, !, %, *, #, ?, &'가 포함되야 합니다.");
    }

    @Test
    void register_fail_invalid_not_contained_password() {
        MemberRegisterRequest request = createRequest("test@example.com", "test1234", "test123", "test", "1999", Gender.SECRET,"01012341234");
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("비밀번호를 올바르게 입력해주세요. 비밀번호는 8-16자에 특수문자 '@, $, !, %, *, #, ?, &'가 포함되야 합니다.");
    }

    @Test
    void register_fail_invalid_name() {
        MemberRegisterRequest request = createRequest("test@example.com", "test1234", "test1234", null, "1999", Gender.SECRET,"01012341234");
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("이름을 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_invalid_birthYear() {
        MemberRegisterRequest request = createRequest("test@example.com", "test1234", "test1234", "test", "199", Gender.SECRET,"01012341234");
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("생년을 올바르게 입력해주세요.");
    }


    @Test
    void register_fail_invalid_gender() {
        MemberRegisterRequest request = createRequest("test@example.com", "test1234", "test1234", "test", "1999", null,"01012341234");
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("성별을 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_present_email() {
        //given
        MemberRegisterRequest registerMember = createRequest("test@example.com", "test1234", "test1234", "test", "1999", Gender.SECRET,"01012341234");
        memberService.register(registerMember);

        MemberRegisterRequest request = createRequest("test@example.com", "test1234", "test", "1999", "01012341234", Gender.MEN,"01012341234");

        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("이미 가입된 이메일입니다.");
    }

    @Test
    void register_success() {
        //given
        MemberRegisterRequest registerMember = createRequest("test@example.com", "test1234", "test1234", "test", "1999", Gender.SECRET,"01012341234");

        //when
        Long memberId = memberService.register(registerMember).getMemberId();

        //then
        assertThat(memberId).isEqualTo(2L);
    }

    @Test
    void when_PasswordChanged_then_verifyField() {
        MemberRegisterRequest registerMember = createRequest("test@example.com", "test1234", "test1234", "test", "1999", Gender.SECRET, "01012341234");
        Long memberId = memberService.register(registerMember).getMemberId();

        Member member = memberService.getMember(memberId);
        memberService.update("test1234", "asdf1234!", "asdf1234!", member);

        assertTrue(passwordEncoder.matches("asdf1234!", member.getPassword()));
    }

    private MemberRegisterRequest createRequest(String email, String password, String passwordConfirm, String name, String birthYear, Gender gender, String phoneNo) {
        return new MemberRegisterRequest(email, password, passwordConfirm, name, birthYear, gender, phoneNo);
    }


}