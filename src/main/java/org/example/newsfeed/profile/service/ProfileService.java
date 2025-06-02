package org.example.newsfeed.profile.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.constant.PasswordFormatConstant;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.common.security.PasswordEncoder;
import org.example.newsfeed.member.entity.Member;
import org.example.newsfeed.member.repository.MemberRepository;
import org.example.newsfeed.profile.dto.FindProfileResponseDto;
import org.example.newsfeed.profile.dto.UpdateProfileRequestDto;
import org.example.newsfeed.profile.dto.UpdateProfileResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.newsfeed.common.exception.error.CustomErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProfileService {

     private final MemberRepository memberRepository;
     private final PasswordEncoder passwordEncoder;
    //프로필 조회
    @Transactional
    public FindProfileResponseDto findProfile(Long id) {
        Member findUser = memberRepository.findById(id).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return new FindProfileResponseDto(findUser.getMemberName(),findUser.getEmail(),findUser.getAge(),findUser.getNickname(), findUser.getIntro(),findUser.getMbti());
    }

    //프로필 수정
    @Transactional
    public UpdateProfileResponseDto updateProfile(Long id, UpdateProfileRequestDto requestDto) {
        //id값으로 레파지토리에서 값을 찾아오고 그 값들을 비교 만약에 id 없으면 오류 내기
        Member updateProfile = memberRepository.findByIdOrElseThrow(id);

        //db패스워드랑 입력받은 비밀번호 확인(본인인증)
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), updateProfile.getPassword())) {
            throw new CustomException(CustomErrorCode.INVALID_PASSWORD);
        }
        //위에 로직 통과 후 바꾸는 비밀번호가 이전과 동일한지 확인
        if (passwordEncoder.matches(requestDto.getNewPassword(), updateProfile.getPassword())) {
            throw new CustomException(CustomErrorCode.NEW_PASSWORD_SAME_AS_OLD);
        }
        // 비밀번호 형식
        if (!PasswordFormatConstant.PASSWORD_REGEX.matcher(requestDto.getNewPassword()).matches()) {
            throw new CustomException(CustomErrorCode.PASSWORD_INVALID_FORMAT);
        }
         //위 로직 통과 후 비밀번호 변경
        updateProfile.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

        //요청 값중 하나라도 널이나 공백이 아니라면 수정이 가능하게 , 대신 아무값이 없으면 db값으로 대체
        //StringUtils.hasText는 널을 포함해 공백이면 펄스를 반환
        //바꿀 값들 email(형식확인필), nickname,intro,mbti

        // 별명
        if (requestDto.getNickname() != null && !requestDto.getNickname().isBlank()) {
            updateProfile.setNickname(requestDto.getNickname());
        }
        // 입력 없으면 기존 값 그대로 유지됨

        // email
        if (requestDto.getEmail() != null && !requestDto.getEmail().isBlank()) {
            if (!PasswordFormatConstant.EMAIL_REGEX.matcher(requestDto.getEmail()).matches()) {
                throw new CustomException(CustomErrorCode.EMAIL_INVALID_FORMAT);
            }
            updateProfile.setEmail(requestDto.getEmail());
        }
        // mbti
        if (requestDto.getMbti() != null && !requestDto.getMbti().isBlank()) {
            updateProfile.setMbti(requestDto.getMbti());
        }
        //intro
        if(requestDto.getIntro()!=null && !requestDto.getIntro().isBlank()){
            updateProfile.setIntro(requestDto.getIntro());
        }

        //  저장
        memberRepository.save(updateProfile);

        return new UpdateProfileResponseDto(updateProfile);
    }
}


