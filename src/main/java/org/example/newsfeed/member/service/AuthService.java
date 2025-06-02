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
import org.example.newsfeed.follow.repository.FollowRepository;
import org.example.newsfeed.member.dto.*;
import org.example.newsfeed.member.entity.Member;
import org.example.newsfeed.member.repository.MemberRepository;
import org.example.newsfeed.post.repository.PostRepository;
import org.springframework.stereotype.Service;

import static org.example.newsfeed.common.constant.PasswordFormatConstant.EMAIL_REGEX;
import static org.example.newsfeed.common.exception.error.CustomErrorCode.EMAIL_NOT_FOUND;
import static org.example.newsfeed.common.exception.error.CustomErrorCode.INVALID_PASSWORD;

/**
 * 인증 관련 로직을 처리하는 서비스 클래스입니다.
 * 회원가입, 로그인, 회원 탈퇴 기능을 제공합니다.
 *
 * @author Hyeonwoo
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입 요청을 처리합니다.
     * 이메일/비밀번호 형식 검증, 중복 체크, 비밀번호 암호화, 회원 정보 저장을 수행합니다.
     *
     * @author Hyeonwoo
     * @param memberName   사용자 이름
     * @param email        사용자 이메일
     * @param password     사용자 비밀번호
     * @param age          사용자 나이
     * @param nickname     사용자 닉네임
     * @param intro        자기소개
     * @param mbti         MBTI (4자리 문자열)
     * @return             생성된 회원 정보를 담은 AuthResponseDto
     * @throws CustomException 유효하지 않은 이메일/비밀번호/MBTI 형식, 중복된 이메일 등 예외 발생
     */
    public AuthResponseDto signup(String memberName, String email, String password, int age, String nickname, String intro, String mbti) {
        // 이메일 형식이 유효하지 않을 경우
        if (!PasswordFormatConstant.EMAIL_REGEX.matcher(email).matches()) {
            throw new CustomException(CustomErrorCode.EMAIL_INVALID_FORMAT);
        }

        // 비밀번호 형식이 유효하지 않을 경우
        if (!PasswordFormatConstant.PASSWORD_REGEX.matcher(password).matches()) {
            throw new CustomException(CustomErrorCode.PASSWORD_INVALID_FORMAT);
        }

        // 삭제됐던 이메일일 경우
        // 중복된 이메일일 경우
        Member existMember = memberRepository.getMemberByEmail(email);
        if(existMember != null){
            if(existMember.isDeleted()){
                throw new CustomException(CustomErrorCode.DELETED_ACCOUNT);
            } else {
                throw new CustomException(CustomErrorCode.EMAIL_ALREADY_EXISTS);
            }
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

    /**
     * 회원 탈퇴 요청을 처리합니다.
     * 이메일 및 비밀번호 검증 후 회원 상태를 '삭제'로 처리하며,
     * 작성한 게시글은 영구 삭제됩니다.
     *
     * @author Hyeonwoo
     * @param email     요청 이메일
     * @param password  요청 비밀번호
     * @param memberId  현재 로그인된 사용자 ID
     * @throws CustomException 이메일 없음, 권한 없음, 비밀번호 불일치 등의 예외 발생
     */
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
        followRepository.deleteByFolloweeId(memberId);
    }

    /**
     * 로그인 요청을 처리하고 JWT 토큰을 발급합니다.
     * 이메일/비밀번호를 검증한 후, 응답 헤더에 토큰을 포함시킵니다.
     *
     * @author Hyeonwoo
     * @param loginRequestDto  로그인 요청 정보 (이메일, 비밀번호)
     * @param response         HttpServletResponse 객체 (JWT 토큰을 헤더에 삽입)
     * @return                 로그인 결과 (회원 ID 및 토큰)를 담은 LoginResponseDto
     * @throws CustomException 이메일 형식 오류, 미존재 이메일, 비밀번호 불일치 시 발생
     */
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