package com.example.demo.web.controller.api.controller;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.dto.response.ModifyMemberResponse;
import com.example.demo.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/password-confirm")
    public ResponseEntity<Object> passwordConfirm(Principal principal, String password){
        Member member = memberService.getMember(principal);

        ModifyMemberResponse response = memberService.matchPasswordThenReturnResponse(password, member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/password")
    public ResponseEntity<Object> updatePassword(Principal principal, String password, String newPassword, String newPasswordConfirm){
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