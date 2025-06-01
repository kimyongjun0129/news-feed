package org.example.newsfeed.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.constant.SessionConstant;
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

    /**
     * @author : hojin
     * @param   requestDto      회원가입 요청 정보를 담은 DTO (이름, 이메일, 비밀번호 등)
     * @return                  생성된 사용자 인증 정보를 담은 ResponseEntity (201 Created)
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody MemberSignupRequestDto requestDto) {
        AuthResponseDto responseDto = authorService.signup(
                requestDto.getMemberName(),
                requestDto.getEmail(),
                requestDto.getPassword()
        );
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * @author : hojin
     * @param   requestDto      탈퇴 요청 정보 (이메일 및 비밀번호)
     * @param   authUser        현재 로그인된 사용자 객체 (Spring Security)
     * @return                  상태코드 200 OK 반환
     */
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

    /**
     * @author : kimyongjun0129
     * @param   requestDto      로그인 요청 정보 (이메일, 비밀번호 등)
     * @param   response        로그인 성공 시 토큰 등을 담기 위한 HttpServletResponse 객체
     * @return                  로그인 결과 정보를 담은 ResponseEntity (200 OK)
     */
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