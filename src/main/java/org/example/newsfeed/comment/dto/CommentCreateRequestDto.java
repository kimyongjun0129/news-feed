package org.example.newsfeed.comment.dto;

import lombok.Getter;

@Getter
public class CommentCreateRequestDto {
    private final String content;

    public CommentCreateRequestDto(String content) {
        this.content = content;
    }
}