package com.example.demo.web.repository;

import com.example.demo.web.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndPhoneNo(String email, String phoneNo);
    List<Member> findByNameAndPhoneNo(String name, String phoneNo);
}
