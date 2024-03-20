package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Member;
import lombok.Data;
import lombok.Getter;

@Data
public class MemberInformationResponse {
    private String name;
    private String email;

    public MemberInformationResponse(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
    }
}
