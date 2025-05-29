package org.example.newsfeed.member.controller;

import org.example.newsfeed.member.dto.MemberDeleteRequestDto;
import org.example.newsfeed.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

}
