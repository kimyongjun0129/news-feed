package org.example.newsfeed.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private final Long id;
    private final String token;
}
