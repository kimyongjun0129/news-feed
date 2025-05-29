package org.example.newsfeed.like.dto;

import lombok.Getter;

@Getter
public class CommentLikeRequestDto {

    private final Long memberId;

    private final Long commentId;

    public CommentLikeRequestDto(Long memberId, Long commentId){
        this.memberId = memberId;
        this.commentId = commentId;
    }

}
