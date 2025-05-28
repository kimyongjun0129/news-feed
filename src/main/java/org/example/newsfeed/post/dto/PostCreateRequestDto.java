package org.example.newsfeed.post.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostCreateRequestDto {
    @NotNull
    @Size(min = 2, max = 30)
    private final String title;

    @NotNull
    @Size(max = 5000)
    private final String content;

    public PostCreateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}