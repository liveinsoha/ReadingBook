package com.example.demo.web.util;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.domain.enums.MemberRole;
import com.example.demo.web.dto.request.MemberRegisterRequest;
import com.example.demo.web.repository.MemberRepository;
import com.example.demo.web.service.BookService;
import com.example.demo.web.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitAdminClass {


    void init(){
       initData();
    }

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    public void initData() {
        MemberRegisterRequest registerRequest = new MemberRegisterRequest(
                "admin@naver.com",
                passwordEncoder.encode("Asdasd!!1"),
                passwordEncoder.encode("Asdasd!!1"),
                "이원준",
                "1999",
                Gender.MEN,
                "01096780573"
        );
        MemberRegisterRequest registerRequest1 = new MemberRegisterRequest(
                "treesheep1@naver.com",
                passwordEncoder.encode("Asdasd!!1"),
                passwordEncoder.encode("Asdasd!!1"),
                "이기제",
                "1999",
                Gender.MEN,
                "01096780573"
        );
        Member member1 = Member.createMember(registerRequest1);
        Member member = Member.createMember(registerRequest);
        member1.setRole(MemberRole.ROLE_VENDOR);
        member.setRole(MemberRole.ROLE_ADMIN);
        memberRepository.save(member);
        memberRepository.save(member1);
    }
}
