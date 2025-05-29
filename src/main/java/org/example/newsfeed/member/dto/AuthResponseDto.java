package org.example.newsfeed.member.dto;

import lombok.Getter;
import org.example.newsfeed.member.entity.Member;

import java.time.LocalDateTime;

@Getter
public class AuthResponseDto {
    private final Long id;
    private final String memberName;
    private final String email;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public AuthResponseDto(Long id, String memberName, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public AuthResponseDto(Member member) {
        this.id = member.getId();
        this.memberName = member.getMemberName();
        this.email = member.getEmail();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
    }
}

