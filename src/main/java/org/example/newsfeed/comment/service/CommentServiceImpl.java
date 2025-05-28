package org.example.newsfeed.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.comment.dto.CommentResponseDto;
import org.example.newsfeed.comment.entity.Comment;
import org.example.newsfeed.comment.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    @Override
    public CommentResponseDto createComment(Long postId, String content, Long userId) {
        Comment comment = new Comment(content, userId, postId);
        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(savedComment);
    }

    @Override
    public Page<CommentResponseDto> findComments(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Comment> commentPage = commentRepository.findAllByPostId(pageable);

        return commentPage.map(CommentResponseDto::new);
    }
}
