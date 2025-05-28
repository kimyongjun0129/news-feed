package org.example.newsfeed.profile.dto;

import lombok.Getter;

@Getter
public class FindProfileResponseDto {
    //프로필 수정 dto
    //로그인한 사용자는 본인의 사용자 정보를 수정할 수 있습니다.
    //
    private final String memberName;
    private final String email;

    public FindProfileResponseDto(String memberName, String email){
        this.memberName = memberName;
        this.email = email;
    }

}
