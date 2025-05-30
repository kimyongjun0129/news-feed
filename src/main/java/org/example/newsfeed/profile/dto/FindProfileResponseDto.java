package org.example.newsfeed.profile.dto;

import lombok.Getter;

@Getter
public class FindProfileResponseDto {

// 식별자를 넣으면 어떤 값이 나오게 할 것인가
    // 닉네임,자기소개
    private String email;
    private int age;
    private final String nickname;
    private final String intro;
    private final String mbti;



    public FindProfileResponseDto(String email,int age,String nickname,String intro,String mbti){
        this.email = email;
       this.age= age;
        this.nickname = nickname;
        this.intro = intro;
        this.mbti =mbti;

    }

}
