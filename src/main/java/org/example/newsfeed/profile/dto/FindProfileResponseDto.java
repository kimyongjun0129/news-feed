package org.example.newsfeed.profile.dto;

import lombok.Getter;

@Getter
public class FindProfileResponseDto {
    //프로필 조회

    private final String memberName;
    private final String email;

    public FindProfileResponseDto(String memberName, String email){
        this.memberName = memberName;
        this.email = email;
    }

}
