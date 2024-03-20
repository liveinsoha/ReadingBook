package com.example.demo.web.service;


import com.example.demo.web.dto.request.MemberRegisterRequest;
import com.example.demo.web.dto.response.MemberInformationResponse;
import com.example.demo.web.dto.response.ModifyMemberResponse;
import com.example.demo.web.dto.response.SignUpSuccessResponse;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BaseException(BaseResponseCode.MEMBER_NOT_FOUND));
    }

    public Member getMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new BaseException(BaseResponseCode.MEMBER_NOT_FOUND));
    }

    public Member getMember(Principal principal) {
        if (principal == null) {
            throw new BaseException(BaseResponseCode.LOGIN_REQUIRED);
        }

        String email = principal.getName();
        return getMember(email);
    }

    public SignUpSuccessResponse register(MemberRegisterRequest request) {
        validatePresentEmail(request.getEmail());
        validateForm(request);

        Member member = Member.createMember(request);

        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.encodePassword(encodedPassword);

        Member savedMember = memberRepository.save(member);
        return new SignUpSuccessResponse(member.getId());
    }


    public void validatePresentEmail(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);

        if (findMember.isPresent()) {
            throw new BaseException(BaseResponseCode.EMAIL_DUPLICATE);
        }
    }

    private static void validateForm(MemberRegisterRequest request) {
        String email = request.getEmail();
        if (email == null) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_EMAIL);
        }

        String[] splitEmail = email.split("@");
        if (splitEmail[0].length() < 4 || splitEmail[0].length() > 24) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_EMAIL);
        }

        String password = request.getPassword();
        String passwordConfirm = request.getPasswordConfirm();
        if (password == null || passwordConfirm == null) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_PASSWORD);
        }

        if ((password.length() < 8 || password.length() > 16) || (passwordConfirm.length() < 8 || passwordConfirm.length() > 16)) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_PASSWORD);
        }

        if (!password.equals(passwordConfirm)) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_PASSWORD);
        }


        String name = request.getName();
        if (name == null) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_NAME);
        }
        if (name.length() > 20 || name.length() < 2) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_NAME);
        }

        String birthYear = request.getBirthYear();
        if (birthYear.length() != 4) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_BIRTH_YEAR);

        }

        Gender gender = request.getGender();
        if (gender == null) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_GENDER);
        }
    }


    /**
     * 비밀번호 확인 이후 DTO 반환 메소드
     *
     * @param rawPassword
     * @param member
     * @return dto
     */
    public ModifyMemberResponse matchPasswordThenReturnResponse(String rawPassword, Member member) {
        String encodedPassword = member.getPassword();

        matchPasswords(rawPassword, encodedPassword);

        return new ModifyMemberResponse(member);
    }

    private void matchPasswords(String rawPassword, String encodedPassword) {
        boolean result = passwordEncoder.matches(rawPassword, encodedPassword);

        if (result == false) {
            throw new BaseException(BaseResponseCode.INVALID_PASSWORD);
        }
    }

    /**
     * 비밀번호 업데이트
     *
     * @param password
     * @param newPassword
     * @param newPasswordConfirm
     * @param member
     */
    @Transactional
    public void update(String password, String newPassword, String newPasswordConfirm, Member member) {
        String encodedPassword = member.getPassword();

        validatePasswords(password, newPassword, newPasswordConfirm);

        matchPasswords(password, encodedPassword);

        String changingPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(changingPassword);
    }

    private void validatePasswords(String password, String newPassword, String newPasswordConfirm) {
        if (password == null || password.trim().equals("")) {
            throw new IllegalArgumentException("현재 비밀번호나 새 비밀번호 또는 새 비밀번호 확인을 입력하세요.");
        }

        if (newPassword == null || newPassword.trim().equals("")) {
            throw new IllegalArgumentException("현재 비밀번호나 새 비밀번호 또는 새 비밀번호 확인을 입력하세요.");
        }

        if (newPasswordConfirm == null || newPasswordConfirm.trim().equals("")) {
            throw new IllegalArgumentException("현재 비밀번호나 새 비밀번호 또는 새 비밀번호 확인을 입력하세요.");
        }

        if (!newPassword.equals(newPasswordConfirm)) {
            throw new IllegalArgumentException("변경할 비밀번호가 일치하지 않습니다.");
        }
    }

    public MemberInformationResponse findMemberInformation(Principal principal) {
        Member member = getMember(principal);
        return new MemberInformationResponse(member);
    }
}