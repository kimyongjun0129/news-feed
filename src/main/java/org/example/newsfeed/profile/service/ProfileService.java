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



}
