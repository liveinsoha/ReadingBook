package com.example.demo.web.dto.request;

import com.example.demo.web.domain.enums.Gender;
import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberRegisterRequest {
    private String email;
    private String password;
    private String passwordConfirm;
    private String name;
    private String birthYear;
    private Gender gender;
    private String phoneNo;
}
