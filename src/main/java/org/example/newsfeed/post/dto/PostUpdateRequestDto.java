package org.example.newsfeed.post.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    @Size(min = 2, max = 20)
    private final String title;

    @Size(max = 5000)
    private final String content;

    public PostUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
