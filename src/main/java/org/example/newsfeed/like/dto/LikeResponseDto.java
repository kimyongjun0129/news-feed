package org.example.newsfeed.like.dto;

import lombok.Getter;
import org.example.newsfeed.like.entity.Like;

/**
 * 게시물 좋아요 생성 시,
 * 멤버 별 좋아요 조회 시
 * 사용하는 response dto
 */
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
