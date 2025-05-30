package org.example.newsfeed.profile.dto;

import lombok.Getter;

@Getter
public class UpdateProfileResponseDto {



    private String memberName;
    private String email;

    public UpdateProfileResponseDto(String memberName, String email){
        this.memberName = memberName;
        this.email = email;
    }

}
