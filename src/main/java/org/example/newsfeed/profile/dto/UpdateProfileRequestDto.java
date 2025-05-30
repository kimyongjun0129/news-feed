package org.example.newsfeed.profile.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class UpdateProfileRequestDto {

//프로필 수정
    private String email;
    private final String nickname;
    private final String intro;
    @Size(min = 4, max = 4, message = "MBTI는 4자리여야 합니다.")
    private final String mbti;

    private String currentPassword; // 사용자 입력 비밀번호
    private String newPassword; //새로운 비밀번호

}
