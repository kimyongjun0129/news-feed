package org.example.newsfeed.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.newsfeed.member.dto.LoginRequestDto;
import org.example.newsfeed.member.dto.LoginResponseDto;
import org.example.newsfeed.member.dto.MemberDeleteRequestDto;
import org.example.newsfeed.member.dto.MemberSignupRequestDto;
import org.example.newsfeed.member.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthService authorService;

    public AuthController(AuthService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody MemberSignupRequestDto requestDto) {
        authorService.signup(requestDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody MemberDeleteRequestDto requestDto) {
        authorService.delete(requestDto);
        return ResponseEntity.ok("회원탈퇴 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletRequest httpServletRequest
    ) {
        LoginResponseDto loginResponseDto = authorService.login(loginRequestDto, httpServletRequest);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }
}
