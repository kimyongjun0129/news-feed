package org.example.newsfeed.member.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.constant.PasswordFormatConstant;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.common.security.PasswordEncoder;
import org.example.newsfeed.member.dto.*;
import org.example.newsfeed.member.entity.Member;
import org.example.newsfeed.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import static org.example.newsfeed.common.constant.PasswordFormatConstant.EMAIL_REGEX;
import static org.example.newsfeed.common.exception.error.CustomErrorCode.EMAIL_NOT_FOUND;
import static org.example.newsfeed.common.exception.error.CustomErrorCode.INVALID_PASSWORD;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponseDto signup(String memberName, String email, String password, int age, String nickname, String intro, String mbti) {
        // 이메일 형식이 유효하지 않을 경우
        if (!PasswordFormatConstant.EMAIL_REGEX.matcher(email).matches()) {
            throw new CustomException(CustomErrorCode.EMAIL_INVALID_FORMAT);
        }

        // 비밀번호 형식이 유효하지 않을 경우
        if (!PasswordFormatConstant.PASSWORD_REGEX.matcher(password).matches()) {
            throw new CustomException(CustomErrorCode.PASSWORD_INVALID_FORMAT);
        }

        // 중복된 사용자 이메일일 경우
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(CustomErrorCode.EMAIL_ALREADY_EXISTS);
        }
        //MBTI 4글자 초과해 입력한 경우
        if(mbti.length()!=4){
            throw new CustomException(CustomErrorCode.MBTI_INVALID_FORMAT);
        }

        // 암호화 및 회원 저장
        String hashedPassword = passwordEncoder.encode(password);
        Member member = new Member(memberName, email, hashedPassword,age,nickname,intro,mbti);
        Member savedMember = memberRepository.save(member);

        return new AuthResponseDto(savedMember);
    }

    public void delete(String email, String password) {
        // MEMBER_NOT_FOUND 예외 없이 처리
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            throw new CustomException(CustomErrorCode.EMAIL_NOT_FOUND); // 조용히 종료하거나 무시 처리
        }

        // 이미 탈퇴한 사용자 이메일인 경우
        if (member.getEmail().startsWith("[DELETED]_")) {
            throw new CustomException(CustomErrorCode.DELETED_ACCOUNT);
        }

        // 사용자 아이디와 비밀번호가 일치하지 않는 경우
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(CustomErrorCode.INVALID_PASSWORD);
        }

        // 이메일 재사용 방지
        member.setEmail("[DELETED]_" + member.getEmail());
        memberRepository.save(member);

        // 실제 삭제
        memberRepository.delete(member);
    }

    public AuthResponseDto login(String email, String password) {

        // 이메일 형식 검증
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new CustomException(CustomErrorCode.EMAIL_INVALID_FORMAT);
        }

        Member member = memberRepository.getMemberByEmail(email);

        // 유저 유무 검증
        if (member == null) {
            throw new CustomException(EMAIL_NOT_FOUND);
        }

        // 비밀 번호 검증
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(INVALID_PASSWORD);
        }

        return new AuthResponseDto(member);
    }
}