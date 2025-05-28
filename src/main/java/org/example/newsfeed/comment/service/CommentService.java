package org.example.newsfeed.comment.service;

import org.example.newsfeed.comment.dto.CommentResponseDto;
import org.springframework.data.domain.Page;

public interface CommentService {
    CommentResponseDto createComment(Long postId, String content, Long userId);

    Page<CommentResponseDto> findComments(Long postId, int page, int size);
}
