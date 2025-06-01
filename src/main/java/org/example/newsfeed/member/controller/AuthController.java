package org.example.newsfeed.member.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.dto.AuthUser;
import org.example.newsfeed.common.filter.JwtUtil;
import org.example.newsfeed.member.dto.*;
import org.example.newsfeed.member.service.AuthService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authorService;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

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
            @AuthenticationPrincipal AuthUser authUser
    ) {
        authorService.delete(
                requestDto.getEmail(),
                requestDto.getPassword(),
                authUser.getId()
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
            @RequestBody LoginRequestDto requestDto,
            HttpServletResponse response
    ) {
        LoginResponseDto loginResponseDto = authorService.login(requestDto, response);

        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    /**
     * @author : kimyongjun0129
     * @param   token           헤더에 들어있는 token 정보
     * @return                  상태코드 200 OK 반환
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String token
    ) throws ServerException {
        String accessToken = jwtUtil.substringToken(token);
        long expiration = jwtUtil.getExpiration(accessToken);

        redisTemplate.opsForValue().set("logout:"+ accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}