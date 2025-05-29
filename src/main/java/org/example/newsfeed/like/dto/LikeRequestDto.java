package org.example.newsfeed.like.dto;

import lombok.Getter;

@Getter
public class LikeRequestDto {

    private final Long memberId;

    private final Long postId;

    public LikeRequestDto(Long memberId, Long postId){
        this.memberId = memberId;
        this.postId = postId;
    }

}
