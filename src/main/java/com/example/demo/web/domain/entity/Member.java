package com.example.demo.web.domain.entity;

import com.example.demo.web.controller.dto.MemberRegisterRequest;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.domain.enums.MemberRole;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;

    private String name;
    private String birthYear;
    private String phoneNo;
    private int point;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MemberRole role;


    public static Member createMember(MemberRegisterRequest request){
        Member member = new Member();
        member.email = request.getEmail();
        member.password = request.getPassword();
        member.name = request.getName();
        member.birthYear = request.getBirthYear();
        member.phoneNo = request.getPhoneNo();
        member.gender = request.getGender();
        return member;
    }

    public void encodePassword(String encodedPassword){
        password = encodedPassword;
    }
}