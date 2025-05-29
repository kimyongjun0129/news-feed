package org.example.newsfeed.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.constant.SessionConstant;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.common.security.PasswordEncoder;
import org.example.newsfeed.member.dto.LoginRequestDto;
import org.example.newsfeed.member.dto.LoginResponseDto;
import org.example.newsfeed.member.entity.Member;
import org.example.newsfeed.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static org.example.newsfeed.common.constant.PasswordFormatConstant.EMAIL_REGEX;
import static org.example.newsfeed.common.exception.error.CustomErrorCode.EMAIL_NOT_FOUND;
import static org.example.newsfeed.common.exception.error.CustomErrorCode.INVALID_PASSWORD;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto requestDto, HttpServletRequest httpServletRequest) {

        // 이메일 형식 검증
        if (!EMAIL_REGEX.matcher(requestDto.getEmail()).matches()) {
            throw new CustomException(CustomErrorCode.EMAIL_INVALID_FORMAT);
        }

        Member member = memberRepository.getMemberByEmail(requestDto.getEmail());

        // 유저 유무 검증
        if (member == null) {
            throw new CustomException(EMAIL_NOT_FOUND);
        }

        // 비밀 번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new CustomException(INVALID_PASSWORD);
        }

        // 세션 설정
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(SessionConstant.MEMBER, member.getId());

        return LoginResponseDto.builder()
                .message("로그인 성공")
                .status(HttpStatus.OK.value())
                .memberId(member.getId())
                .memberName(member.getMemberName())
                .build();
    }
}