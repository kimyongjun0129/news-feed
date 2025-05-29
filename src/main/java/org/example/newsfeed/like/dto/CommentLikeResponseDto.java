package org.example.newsfeed.like.dto;

import lombok.Getter;
import org.example.newsfeed.like.entity.CommentLike;
import org.example.newsfeed.like.entity.Like;

@Getter
public class CommentLikeResponseDto {
    private Long id;

    private Long memberId;

    private Long commentId;

    public CommentLikeResponseDto(Long id, Long memberId, Long commentId){
        this.id = id;
        this.memberId = memberId;
        this.commentId = commentId;
    }

    public static CommentLikeResponseDto toDto(CommentLike commentLike){
        return new CommentLikeResponseDto(commentLike.getId(), commentLike.getMemberId(), commentLike.getCommentId());
    }
}
