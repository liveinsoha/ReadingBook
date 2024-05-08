package com.example.demo.web.controller.api.controller;

import com.example.demo.config.security.MemberDetails;
import com.example.demo.config.security.MemberDetailsService;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.enums.MemberRole;
import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.dto.response.ModifyMemberResponse;
import com.example.demo.web.service.MemberService;
import com.example.demo.web.service.email.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/send-email")
    public ResponseEntity<Object> sendSellerConfirmEmail(Principal principal, String password) {
        Member member = memberService.getMember(principal);
        memberService.sendSellerConfirmEmail(password, member);

        BaseResponse response = new BaseResponse(
                HttpStatus.OK, "확인 코드를 전송하였습니다.", true
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/verify-seller-code")
    public ResponseEntity<Object> confirmSellerCode(Principal principal, String verificationCode) {
        Member member = memberService.getMember(principal);
        memberService.confirmSellerCode(verificationCode, member);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        System.out.println("email = " + email);

        List<GrantedAuthority> updatedAuthority = new ArrayList<>(authentication.getAuthorities());
        updatedAuthority.add(new SimpleGrantedAuthority(MemberRole.ROLE_ADMIN.toString()));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(email, authentication.getCredentials(), updatedAuthority);
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        // 관리자 권한을 추가하여 세션 정보를 업데이트
        BaseResponse response = new BaseResponse(
                HttpStatus.OK, "판매자 승인 되었습니다.", true
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(true);
    }


    @PostMapping("/password-confirm")
    public ResponseEntity<Object> passwordConfirm(Principal principal, String password) {
        Member member = memberService.getMember(principal);

        ModifyMemberResponse response = memberService.matchPasswordThenReturnResponse(password, member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/password")
    public ResponseEntity<Object> updatePassword(Principal principal, String password, String newPassword, String newPasswordConfirm) {
        Member member = memberService.getMember(principal);

        memberService.update(password, newPassword, newPasswordConfirm, member);

        BaseResponse response = new BaseResponse(
                HttpStatus.OK, "비밀번호가 변경되었습니다.", true
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}