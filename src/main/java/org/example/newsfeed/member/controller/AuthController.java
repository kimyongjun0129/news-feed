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
    private final JwtUtil jwtUtil;

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
    public ResponseEntity<Void> delete(@RequestBody MemberDeleteRequestDto requestDto) {
        authorService.delete(
                requestDto.getEmail(),
                requestDto.getPassword()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletRequest httpServletRequest, // 로그인 IP 파악같은 기능에 사용할 수 있을듯
            HttpServletResponse httpServletResponse
    ) {
        AuthResponseDto responseDto = authorService.login(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword()
        );

        // login memberId 토큰에 삽입
        String token = jwtUtil.generateToken(responseDto.getId());
        Cookie cookie = new Cookie(SessionConstant.TOKEN, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);

        // 토큰을 포함한 쿠키 Response 헤더에 포함
        httpServletResponse.addCookie(cookie);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

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