package org.example.newsfeed.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    @Size(min = 1, max = 255)
    private final String content;

    public CommentRequestDto(String content) {
        this.content = content;
    }
}