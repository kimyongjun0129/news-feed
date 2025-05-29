package org.example.newsfeed.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.newsfeed.common.constant.SessionConstant;
import org.example.newsfeed.member.dto.*;
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
    public ResponseEntity<String> login(
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        String token = authorService.login(loginRequestDto, httpServletRequest);

        Cookie cookie = new Cookie(SessionConstant.TOKEN, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);

        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok("로그인 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        Cookie cookie = new Cookie(SessionConstant.TOKEN, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        httpServletResponse.addCookie(cookie);
        
        return ResponseEntity.ok("로그아웃 완료");
    }
}
