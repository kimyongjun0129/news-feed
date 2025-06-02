package org.example.newsfeed.profile.dto;

import lombok.Getter;
import org.example.newsfeed.member.entity.Member;

@Getter
public class UpdateProfileResponseDto {
    private final String memberName;
    private final String email;
    private final String nickname;
    private final String intro;
    private final String mbti;

    public UpdateProfileResponseDto(Member updateProfile) {
        this.memberName = updateProfile.getMemberName();
        this.email = updateProfile.getEmail();
        this.nickname = updateProfile.getNickname();
        this.intro = updateProfile.getIntro();
        this.mbti = updateProfile.getMbti();
    }
}
