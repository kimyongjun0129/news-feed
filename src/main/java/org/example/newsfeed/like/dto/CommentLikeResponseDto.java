package org.example.newsfeed.like.dto;

import lombok.Getter;

/**
 * 댓글 좋아요 생성 시 사용하는 response dto
 */
@Getter
public class CommentLikeResponseDto {
    private final Long id;

    private final Long memberId;

    private final Long commentId;

    public CommentLikeResponseDto(Long id, Long memberId, Long commentId){
        this.id = id;
        this.memberId = memberId;
        this.commentId = commentId;
    }
}
