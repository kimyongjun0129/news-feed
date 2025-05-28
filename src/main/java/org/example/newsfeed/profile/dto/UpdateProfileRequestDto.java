package org.example.newsfeed.profile.dto;

import lombok.Getter;

@Getter
public class UpdateProfileRequestDto {
    private String memberName;
    private String email;
    private String password;

    private String currentPassword; // 사용자 입력 비밀번호
    private String newPassword; //새로운 비밀번호

}
