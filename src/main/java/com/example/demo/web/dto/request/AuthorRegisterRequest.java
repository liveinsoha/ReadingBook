package com.example.demo.web.dto.request;

import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorRegisterRequest {
    private String name;
    private AuthorOption authorOption;
    private String nationality;
    private String description;
    private String birthYear;
    private Gender gender;
}