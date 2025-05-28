package org.example.newsfeed.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class MemberSignupRequestDto {
    private String memberName;
    private String email;
    private String password;
}