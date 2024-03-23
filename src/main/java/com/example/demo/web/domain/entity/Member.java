package com.example.demo.web.domain.entity;

import com.example.demo.web.dto.request.MemberRegisterRequest;
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


    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MemberRole role;


    public static Member createMember(MemberRegisterRequest request) {
        Member member = new Member();
        member.email = request.getEmail();
        member.password = request.getPassword();
        member.name = request.getName();
        member.birthYear = request.getBirthYear();
        member.gender = request.getGender();
        member.role = MemberRole.ROLE_MEMBER;
        member.phoneNo = request.getPhoneNo();
        return member;
    }

    public void encodePassword(String encodedPassword) {
        password = encodedPassword;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}