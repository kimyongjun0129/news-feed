package org.example.newsfeed.profile.service;

import lombok.RequiredArgsConstructor;
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

    //속성
    private final ProfileRepository profileRepository;

    @Transactional
    //다른사람 프로필 조회 메서드
    public FindProfileResponseDto findProfile(Long id) {
        Member findUser = profileRepository.findByIdOrElseThrow(id);
        return new FindProfileResponseDto(findUser.getMemberName(), findUser.getEmail());
    }


    @Transactional
    public UpdateProfileResponseDto updateprofile(Long id, UpdateProfileRequestDto requestDto) {
        Member updateProfile = profileRepository.findByIdOrElseThrow(id);
        //비밀번호 같은 경우 변경 불가능
        if (!requestDto.getPassword().equals(updateProfile.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 변경할 수 없습니다.");
        }
        //신원확인을 위해 현재 비밀번호 입력후 맞으면 변경 가능.
        return new UpdateProfileResponseDto();


    }
}
