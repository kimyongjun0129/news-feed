package org.example.newsfeed.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProfileRequestDto {


     private String memberName;
     private String email;
      private String currentPassword; // 사용자 입력 비밀번호
      private String newPassword; //새로운 비밀번호

}
