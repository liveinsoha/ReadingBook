package com.example.demo.web.service;


import com.example.demo.web.dto.request.MemberRegisterRequest;
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

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpSuccessResponse register(MemberRegisterRequest request){
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

        if(findMember.isPresent()){
            throw new BaseException(BaseResponseCode.EMAIL_DUPLICATE);
        }
    }

    private static void validateForm(MemberRegisterRequest request) {
        String email = request.getEmail();
        if(email == null){
            throw new BaseException(BaseResponseCode.INVALID_FORM_EMAIL);
        }

        String[] splitEmail = email.split("@");
        if(splitEmail[0].length() < 4 || splitEmail[0].length() > 24){
            throw new BaseException(BaseResponseCode.INVALID_FORM_EMAIL);
        }

        String password = request.getPassword();
        String passwordConfirm = request.getPasswordConfirm();
        if(password == null || passwordConfirm == null){
            throw new BaseException(BaseResponseCode.INVALID_FORM_PASSWORD);
        }

        if((password.length() < 8 || password.length() > 16) || (passwordConfirm.length() < 8 || passwordConfirm.length() > 16)){
            throw new BaseException(BaseResponseCode.INVALID_FORM_PASSWORD);
        }

        if(!password.equals(passwordConfirm)){
            throw new BaseException(BaseResponseCode.INVALID_FORM_PASSWORD);
        }


        String name = request.getName();
        if(name == null){
            throw new BaseException(BaseResponseCode.INVALID_FORM_NAME);
        }
        if(name.length() > 20 || name.length() < 2){
            throw new BaseException(BaseResponseCode.INVALID_FORM_NAME);
        }

        String birthYear = request.getBirthYear();
        if(birthYear.length() != 4){
            throw new BaseException(BaseResponseCode.INVALID_FORM_BIRTH_YEAR);

        }

        Gender gender = request.getGender();
        if(gender == null){
            throw new BaseException(BaseResponseCode.INVALID_FORM_GENDER);
        }
    }
}