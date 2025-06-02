package org.example.newsfeed.like.dto;

import lombok.Getter;

import java.util.List;

/**
 * 게시물 좋아요 조회,
 * 댓글 좋아요 조회 시
 * 사용하는 response dto
 *
 * 좋아요 한 멤버 id 리스트,
 * 게시물 or 댓글의 좋아요 개수,
 * 유저의 좋아요 여부
 */
@Getter
public class PostOrCommentLikesResponseDto {

    private final List<Long> memberIds;

    private Long countLikes;

    private boolean likedByMe;

    public PostOrCommentLikesResponseDto(List<Long> memberIds, Long countLikes, boolean likedByMe){
        this.memberIds = memberIds;
        this.countLikes = countLikes;
        this.likedByMe = likedByMe;
    }

}
