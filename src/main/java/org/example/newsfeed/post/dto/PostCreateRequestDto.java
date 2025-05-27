package org.example.newsfeed.post.dto;

import lombok.Getter;

@Getter
public class PostCreateRequestDto {
    private final String title;
    private final String content;

    public PostCreateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
