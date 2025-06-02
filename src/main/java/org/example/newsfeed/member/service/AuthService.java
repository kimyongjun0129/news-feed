package org.example.newsfeed.member.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.constant.PasswordFormatConstant;
import org.example.newsfeed.common.constant.UserRole;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.common.filter.JwtUtil;
import org.example.newsfeed.common.security.PasswordEncoder;
import org.example.newsfeed.member.dto.*;
import org.example.newsfeed.member.entity.Member;
import org.example.newsfeed.member.repository.MemberRepository;
import org.example.newsfeed.post.repository.PostRepository;
import org.springframework.stereotype.Service;

import static org.example.newsfeed.common.constant.PasswordFormatConstant.EMAIL_REGEX;
import static org.example.newsfeed.common.exception.error.CustomErrorCode.EMAIL_NOT_FOUND;
import static org.example.newsfeed.common.exception.error.CustomErrorCode.INVALID_PASSWORD;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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

        // 삭제됐던 이메일일 경우
        if (memberRepository.existsByEmail("[DELETED]_" + email)) {
            throw new CustomException(CustomErrorCode.DELETED_ACCOUNT);
        }

        // 암호화 및 회원 저장
        String hashedPassword = passwordEncoder.encode(password);
        Member member = new Member(memberName, email, hashedPassword,age,nickname,intro,mbti);
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

        // 본인 이메일인지 확인
        if(!member.getId().equals(memberId)) {
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_ACTION, "본인이 아닙니다");
        }

        // 비밀번호가 일치하지 않는 경우
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(CustomErrorCode.INVALID_PASSWORD);
        }

        // 이메일 재사용 방지
        // member 는 db 에서 삭제되지 않지만 가지고 있던 post 는 삭제
        member.delete();
        postRepository.deleteByMemberId(memberId);
    }

    public LoginResponseDto login(
            LoginRequestDto loginRequestDto,
            HttpServletResponse response
    ) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

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

        String token = jwtUtil.createToken(member.getId(), member.getEmail(), UserRole.ADMIN);
        response.setHeader("Authorization", token);

        return new LoginResponseDto(member.getId(), token);
    }
}