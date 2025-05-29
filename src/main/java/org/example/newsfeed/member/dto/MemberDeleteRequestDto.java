package org.example.newsfeed.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MemberDeleteRequestDto {
    private String email;
    private String password;
}

