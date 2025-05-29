package org.example.newsfeed.like.dto;

import lombok.Getter;

import java.util.List;

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
