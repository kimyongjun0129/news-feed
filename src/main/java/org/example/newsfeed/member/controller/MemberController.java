package org.example.newsfeed.member.controller;

import org.example.newsfeed.member.dto.MemberDeleteRequestDto;
import org.example.newsfeed.member.dto.MemberSignupRequestDto;
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

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody MemberSignupRequestDto requestDto) {
        memberService.signup(requestDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody MemberDeleteRequestDto requestDto) {
        memberService.delete(requestDto);
        return ResponseEntity.ok("회원탈퇴 완료");
    }
}
