package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.request.MemberRegisterRequest;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.exception.BaseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;



@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void register_fail_null(){
        MemberRegisterRequest request = createRequest(null, null, null, null, null, null);
        assertThatThrownBy(
                ()-> memberService.register(request))
                .isInstanceOf(BaseException.class);
    }

    @Test
    void register_fail_invalid_email(){
        MemberRegisterRequest request = createRequest("tes@example.com", "test1234", "test", "1999", "01012341234", Gender.SECRET);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("이메일을 올바르게 입력해주세요.");

    }

    @Test
    void register_fail_invalid_short_password(){
        MemberRegisterRequest request = createRequest("test@example.com", "test", "test", "1999", "01012341234", Gender.SECRET);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("비밀번호를 올바르게 입력해주세요. 비밀번호는 8-16자에 특수문자 '@, $, !, %, *, #, ?, &'가 포함되야 합니다.");
    }

    @Test
    void register_fail_invalid_not_contained_password(){
        MemberRegisterRequest request = createRequest("test@example.com", "test", "test1234", "1999", "01012341234", Gender.SECRET);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("비밀번호를 올바르게 입력해주세요. 비밀번호는 8-16자에 특수문자 '@, $, !, %, *, #, ?, &'가 포함되야 합니다.");
    }

    @Test
    void register_fail_invalid_name(){
        MemberRegisterRequest request = createRequest("test@example.com", "test1234", "t", "1999", "01012341234", Gender.SECRET);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("이름을 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_invalid_birthYear(){
        MemberRegisterRequest request = createRequest("test@example.com", "test1234!", "test", "19990115", "01012341234", Gender.SECRET);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("생년을 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_invalid_phoneNo(){
        MemberRegisterRequest request = createRequest("test@example.com", "test1234!", "test", "1999", "0101234", Gender.SECRET);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("핸드폰 번호를 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_invalid_gender(){
        MemberRegisterRequest request = createRequest("test@example.com", "test1234!", "test", "1999", "01012341234", null);
        assertThatThrownBy(()->memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("성별을 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_present_email(){
        //given
        MemberRegisterRequest registerMember = createRequest("test@example.com", "test1234!", "test", "1999", "01012341234", Gender.SECRET);
        memberService.register(registerMember);

        MemberRegisterRequest request = createRequest("test@example.com", "test1234", "test", "1999", "01012341234", Gender.MEN);

        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("이미 가입된 이메일입니다.");
    }

    @Test
    void register_success(){
        //given
        MemberRegisterRequest registerMember = createRequest("success@example.com", "test1234!", "test", "1999", "01012341234", Gender.SECRET);

        //when
        Long memberId = memberService.register(registerMember).getMemberId();

        //then
        assertThat(memberId).isEqualTo(2L);
    }


    private MemberRegisterRequest createRequest(String email, String password, String name, String birthYear, String phoneNo, Gender gender){
        return new MemberRegisterRequest(email, password, name, birthYear, phoneNo, gender);
    }



}