package org.example.newsfeed.comment.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.newsfeed.comment.entity.Comment;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final String content;
    private final Long memberId;
    private final Long postId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    @Setter
    private Long likeCount;

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.memberId = comment.getMember().getId();
        this.postId = comment.getPost().getId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
