package org.example.newsfeed.member.service;

import org.example.newsfeed.common.constant.PasswordFormatConstant;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.common.security.PasswordEncoder;
import org.example.newsfeed.member.dto.MemberDeleteRequestDto;
import org.example.newsfeed.member.dto.MemberSignupRequestDto;
import org.example.newsfeed.member.entity.Member;
import org.example.newsfeed.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(MemberSignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        String name = requestDto.getMemberName();

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

        // 암호화 및 회원 저장
        String hashedPassword = passwordEncoder.encode(password);
        Member member = new Member(name, email, hashedPassword);
        memberRepository.save(member);
    }

    public void delete(MemberDeleteRequestDto requestDto) {
        // MEMBER_NOT_FOUND 예외 없이 처리
        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElse(null);
        if (member == null) {
            throw new CustomException(CustomErrorCode.EMAIL_NOT_FOUND); // 조용히 종료하거나 무시 처리
        }

        // 이미 탈퇴한 사용자 이메일인 경우
        if (member.getEmail().startsWith("[DELETED]_")) {
            throw new CustomException(CustomErrorCode.DELETED_ACCOUNT);
        }

        // 사용자 아이디와 비밀번호가 일치하지 않는 경우
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new CustomException(CustomErrorCode.INVALID_PASSWORD);
        }

        // 이메일 재사용 방지
        member.setEmail("[DELETED]_" + member.getEmail());
        memberRepository.save(member);

        // 실제 삭제
        memberRepository.delete(member);
    }
}
