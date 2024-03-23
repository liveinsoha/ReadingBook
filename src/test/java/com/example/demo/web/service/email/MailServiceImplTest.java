package com.example.demo.web.service.email;

import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.dto.request.MemberRegisterRequest;
import com.example.demo.web.repository.MemberRepository;
import com.example.demo.web.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
class MailServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MailService mailService = mock(MailService.class);

    private MemberService memberService;

    @BeforeEach
    void beforeEach(){
        memberService = new MemberService(memberRepository, passwordEncoder, mailService);
    }

    @Test
    void whenFoundPassword_thenVerifyEmailSending(){
        //given
        MemberRegisterRequest request = new MemberRegisterRequest("test@gmail.com", "testtest1234", "testtest1234", "홍길동", "1999", Gender.SECRET, "01055555555");
        memberService.register(request);

        //when
        String email = "test@gmail.com";
        String tempPassword = memberService.changePasswordAndSendEmail(email, "01055555555");

        //then
        verify(mailService).send(email, tempPassword);
    }
}