package org.example.newsfeed.like.dto;

import lombok.Getter;
import org.example.newsfeed.like.entity.Like;

@Getter
public class LikeResponseDto {
    private final Long id;

    private final Long memberId;

    private final Long postId;

    public LikeResponseDto(Long id, Long memberId, Long postId){
        this.id = id;
        this.memberId = memberId;
        this.postId = postId;
    }

    public static LikeResponseDto toDto(Like like){
        return new LikeResponseDto(like.getId(), like.getMemberId(), like.getPostId());
    }
}
