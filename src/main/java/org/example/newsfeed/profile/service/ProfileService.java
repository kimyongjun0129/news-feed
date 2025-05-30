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
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ProfileService {


    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public FindProfileResponseDto findProfile(Long id) {
        Member findUser = profileRepository.findByIdOrElseThrow(id);
        return new FindProfileResponseDto(findUser.getMemberName(), findUser.getEmail());
    }

    @Transactional
    public UpdateProfileResponseDto UpdateProfile(Long id, UpdateProfileRequestDto requestDto) {
        //id값으로 레파지토리에서 값을 찾아오고 그 값들을 비교 만약에 id 없으면 오류 내기
        Member updateProfile = profileRepository.findByIdOrElseThrow(id);

        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), updateProfile.getPassword())) {
            throw new CustomException(CustomErrorCode.INVALID_PASSWORD);
        }

        if (passwordEncoder.matches(requestDto.getNewPassword(), updateProfile.getPassword())) {
            throw new CustomException(CustomErrorCode.NEW_PASSWORD_SAME_AS_OLD);
        }

        if (!PasswordFormatConstant.PASSWORD_REGEX.matcher(requestDto.getNewPassword()).matches()) {
            throw new CustomException(CustomErrorCode.PASSWORD_INVALID_FORMAT);
        }
         //위 로직 통과 후 비밀번호 변경
        updateProfile.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

        //요청 값중 하나라도 널이나 공백이 아니라면 수정이 가능하게 , 대신 아무값이 없으면 db값을 보여줌.
        if (StringUtils.hasText(requestDto.getMemberName())) {
            updateProfile.setMemberName(requestDto.getMemberName());
        }
        if (StringUtils.hasText(requestDto.getEmail())) {
            updateProfile.setEmail(requestDto.getEmail());
        }

        // 7. 저장
        profileRepository.save(updateProfile);


// 실제 DB에 저장된 값으로 응답 DTO 생성
        return new UpdateProfileResponseDto(updateProfile.getMemberName(), updateProfile.getEmail());
        }



    }
//            if (requestDto.getMemberName() != null && !requestDto.getMemberName().isBlank()) {
//                updateProfile.setMemberName(requestDto.getMemberName());
//            }
//            if (requestDto.getEmail() != null && !requestDto.getEmail().isBlank()) {
//                updateProfile.setEmail(requestDto.getEmail());

//        updateProfile.setNickname(requestDto.getNickname());
//        updateProfile.setIntro(requestDto.getIntro());
//        updateProfile.setAge(requestDto.getAge());
//        updateProfile.setMbti(requestDto.getMbti());
// return new UpdateProfileResponseDto(requestDto.getNickname(), updateProfile.getIntro(),updateProfile.getAge(), updateProfile.getMbti());