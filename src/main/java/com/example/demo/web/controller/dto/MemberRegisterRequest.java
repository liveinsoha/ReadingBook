package com.example.demo.web.controller.dto;

import com.example.demo.web.domain.enums.Gender;
import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberRegisterRequest {
    private String email;
    private String password;
    private String name;
    private String birthYear;
    private String phoneNo;
    private Gender gender;
}
