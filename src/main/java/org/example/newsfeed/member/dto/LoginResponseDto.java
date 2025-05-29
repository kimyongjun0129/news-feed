package org.example.newsfeed.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String message;
    private int status;
    private Long memberId;
    private String memberName;
}

