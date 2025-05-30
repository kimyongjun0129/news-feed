package org.example.newsfeed.profile.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.constant.PasswordFormatConstant;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.common.security.PasswordEncoder;
import org.example.newsfeed.member.entity.Member;
import org.example.newsfeed.profile.dto.FindProfileResponseDto;
import org.example.newsfeed.profile.dto.UpdateProfileRequestDto;
import org.example.newsfeed.profile.dto.UpdateProfileResponseDto;
import org.example.newsfeed.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {


    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public FindProfileResponseDto findProfile(Long id) {
        Member findUser = profileRepository.findByIdOrElseThrow(id);
        return new FindProfileResponseDto(findUser.getEmail(),findUser.getAge(),findUser.getNickname(), findUser.getIntro(),findUser.getMbti());
    }

    @Transactional
    public UpdateProfileResponseDto UpdateProfile(Long id, UpdateProfileRequestDto requestDto) {
        //id값으로 레파지토리에서 값을 찾아오고 그 값들을 비교 만약에 id 없으면 오류 내기
        Member updateProfile = profileRepository.findByIdOrElseThrow(id);
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

        //요청 값중 하나라도 널이나 공백이 아니라면 수정이 가능하게 , 대신 아무값이 없으면 db값을 보여줌.
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

        //  저장
        profileRepository.save(updateProfile);

        return new UpdateProfileResponseDto(updateProfile);
    }
//        if (StringUtils.hasText(requestDto.get())) {
//            updateProfile.setMemberName(requestDto.getMemberName());
//            // 위에 조건의 값이 들어있다면 db에 값을 요청값으로 세팅해라
//        }
//        if (StringUtils.hasText(requestDto.getEmail())) {
//            updateProfile.setEmail(requestDto.getEmail());
//        }

//        private String email;
//        private int age;
//        private final String nickname;
//        private final String intro;
//        private final String mbti;
        // 7. 저장
//        profileRepository.save(updateProfile);
//
//
//// 실제 DB에 저장된 값으로 응답 DTO 생성
//        return new UpdateProfileResponseDto(updateProfile.getMemberName(), updateProfile.getEmail());
//        }



    }


//        updateProfile.setNickname(requestDto.getNickname());
//        updateProfile.setIntro(requestDto.getIntro());
//        updateProfile.setAge(requestDto.getAge());
//        updateProfile.setMbti(requestDto.getMbti());
// return new UpdateProfileResponseDto(requestDto.getNickname(), updateProfile.getIntro(),updateProfile.getAge(), updateProfile.getMbti());