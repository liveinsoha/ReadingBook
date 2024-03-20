package com.example.demo.web.dto.response;

import com.example.demo.web.domain.entity.Member;
import lombok.Getter;

@Getter
public class ModifyMemberResponse {
    private Long memberId;
    private String name;
    private String email;

    public ModifyMemberResponse(Member member) {
        this.memberId = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
    }
}