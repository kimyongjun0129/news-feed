package org.example.newsfeed.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.constant.SessionConstant;
import org.example.newsfeed.common.filter.JwtUtil;
import org.example.newsfeed.member.dto.*;
import org.example.newsfeed.member.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authorService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody MemberSignupRequestDto requestDto) {
        AuthResponseDto responseDto = authorService.signup(
                requestDto.getMemberName(),
                requestDto.getEmail(),
                requestDto.getPassword()
        );
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(
            @RequestBody MemberDeleteRequestDto requestDto,
            @RequestAttribute("memberId") Long memberId
    ) {
        authorService.delete(
                requestDto.getEmail(),
                requestDto.getPassword(),
                memberId
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletResponse response
    ) {
        LoginResponseDto loginResponseDto = authorService.login(loginRequestDto, response);

        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    // 현재 로그아웃 해도 토큰을 가지고 있으면 로그인 가능
    // 블랙리스트 지정, access + refresh 토큰 방식으로 개선 가능
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        Cookie cookie = new Cookie(SessionConstant.TOKEN, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        httpServletResponse.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}