package com.example.demo.config.security;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.enums.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class MemberDetails implements UserDetails {
    private Member member;

    public MemberDetails(Member member) {
        this.member = member;
    }

    public void addAuthority() {
        // 이 메서드를 통해 새로운 권한을 authorities에 추가합니다.
        // 여기서는 간단히 새로운 SimpleGrantedAuthority를 생성하여 authorities에 추가하는 방식을 사용합니다.
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(MemberRole.ROLE_ADMIN.toString()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole().toString()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}