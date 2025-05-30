package org.example.newsfeed.profile.dto;

import lombok.Getter;

@Getter
public class FindProfileResponseDto {

// 식별자를 넣으면 어떤 값이 나오게 할 것인가
    // 닉네임,자기소개
    private final String memberName;
    private final String email;


    public FindProfileResponseDto(String memberName, String email){
        this.memberName= memberName;
        this.email = email;

    }

}
