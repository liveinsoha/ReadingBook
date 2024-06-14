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

@Component
@RequiredArgsConstructor
public class InitAdminClass {

    @PostConstruct
    void init(){
       initData();
    }

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    public void initData() {
        MemberRegisterRequest registerRequest1 = new MemberRegisterRequest(
                "admin@naver.com",
                passwordEncoder.encode("Asdasd!!1"),
                passwordEncoder.encode("Asdasd!!1"),
                "이원준",
                "1999",
                Gender.MEN,
                "01096780573"
        );
        MemberRegisterRequest registerRequest2 = new MemberRegisterRequest(
                "wonjun88888@gmail.com",
                passwordEncoder.encode("Asdasd!!1"),
                passwordEncoder.encode("Asdasd!!1"),
                "이기제",
                "1999",
                Gender.MEN,
                "01096780573"
        );
        MemberRegisterRequest registerRequest3 = new MemberRegisterRequest(
                "treesheep@naver.com",
                passwordEncoder.encode("Asdasd!!1"),
                passwordEncoder.encode("Asdasd!!1"),
                "전희수",
                "1999",
                Gender.MEN,
                "01096780573"
        );
        Member vendor = Member.createMember(registerRequest1);
        Member admin = Member.createMember(registerRequest2);
        Member member = Member.createMember(registerRequest3);

        vendor.setRole(MemberRole.ROLE_VENDOR);
        admin.setRole(MemberRole.ROLE_ADMIN);
        member.setRole(MemberRole.ROLE_MEMBER);

        memberRepository.save(vendor);
        memberRepository.save(admin);
        memberRepository.save(member);
    }
}
