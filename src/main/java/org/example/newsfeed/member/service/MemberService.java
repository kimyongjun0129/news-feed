package org.example.newsfeed.member.service;

import org.example.newsfeed.common.security.PasswordEncoder;
import org.example.newsfeed.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


}
