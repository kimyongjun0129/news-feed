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
        return new FindProfileResponseDto(findUser.getMemberName(), findUser.getEmail());
    }

    @Transactional
    public UpdateProfileResponseDto UpdateProfile(Long id, UpdateProfileRequestDto requestDto) {
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

        updateProfile.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        updateProfile.setMemberName(requestDto.getMemberName());
        updateProfile.setEmail(requestDto.getEmail());


        return new UpdateProfileResponseDto(updateProfile.getMemberName(), updateProfile.getEmail());
    }
}