package org.example.newsfeed.comment.service;

import org.example.newsfeed.comment.dto.CommentResponseDto;
import org.springframework.data.domain.Page;

public interface CommentService {
    CommentResponseDto createComment(Long postId, String content, Long memberId);

    Page<CommentResponseDto> findComments(Long postId, int page, int size);

    CommentResponseDto updateComment(Long postId, Long commentId, String content, Long memberId);

    void deleteComment(Long postId, Long commentId, Long memberId);
}
