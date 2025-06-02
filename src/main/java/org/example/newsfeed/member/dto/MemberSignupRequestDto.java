package org.example.newsfeed.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RequiredArgsConstructor
@Setter
@Getter
public class MemberSignupRequestDto {
    //회원가입
    private String memberName;
    private String email;
    private String password;
    private int age;
    private String nickname;
    private String intro;
    private String mbti;

}
