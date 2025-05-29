package org.example.newsfeed.like.dto;

import lombok.Getter;

@Getter
public class LikeCountResponseDto {

    private final Long postId;

    private final int count;

    public LikeCountResponseDto(Long postId, int count){
        this.postId = postId;
        this.count = count;
    }

}
