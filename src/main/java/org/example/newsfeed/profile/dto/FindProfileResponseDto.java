package org.example.newsfeed.profile.dto;

import lombok.Getter;

@Getter
public class FindProfileResponseDto {


    private final String memberName;
    private final String email;

    public FindProfileResponseDto(String memberName, String email){
        this.memberName = memberName;
        this.email = email;
    }

}
