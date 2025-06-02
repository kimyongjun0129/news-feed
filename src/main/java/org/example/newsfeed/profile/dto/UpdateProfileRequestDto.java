package org.example.newsfeed.profile.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateProfileRequestDto {

    //프로필 수정
    private String memberName;
    private String email;
    private String nickname;
    private String intro;
    @Size(min = 4, max = 4, message = "MBTI는 4자리여야 합니다.")
    private String mbti;

    private String currentPassword; // 사용자 입력 비밀번호
    private String newPassword; //새로운 비밀번호
}
