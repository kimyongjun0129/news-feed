package org.example.newsfeed.member.service;

import jakarta.transaction.Transactional;
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

    public AuthResponseDto signup(String memberName, String email, String password) {
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

        // 삭제됐던 이메일일 경우
        if (memberRepository.existsByEmail("[DELETED]_" + email)) {
            throw new CustomException(CustomErrorCode.DELETED_ACCOUNT);
        }

        // 암호화 및 회원 저장
        String hashedPassword = passwordEncoder.encode(password);
        Member member = new Member(memberName, email, hashedPassword);
        Member savedMember = memberRepository.save(member);

        return new AuthResponseDto(savedMember);
    }

    @Transactional
    public void delete(String email, String password, Long memberId) {
        // MEMBER_NOT_FOUND 예외 없이 처리
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            throw new CustomException(CustomErrorCode.EMAIL_NOT_FOUND); // 조용히 종료하거나 무시 처리
        }

        // 본인 이메일인지 확인, 메세지 본인이 아닙니다로 바꾸기 희망
        if(!member.getId().equals(memberId)) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_ACTION);
        }

        // 사용자 아이디와 비밀번호가 일치하지 않는 경우
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(CustomErrorCode.INVALID_PASSWORD);
        }

        // 이메일 재사용 방지
        Member deletedMember = new Member(member.getMemberName(), "[DELETED]_" + member.getEmail(), member.getPassword());
        memberRepository.save(deletedMember);

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