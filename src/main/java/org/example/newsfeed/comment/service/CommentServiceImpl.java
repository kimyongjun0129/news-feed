package org.example.newsfeed.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.comment.dto.CommentResponseDto;
import org.example.newsfeed.comment.entity.Comment;
import org.example.newsfeed.comment.repository.CommentRepository;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
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
    public CommentResponseDto createComment(Long postId, String content, Long memberId) {
        Comment comment = new Comment(content, memberId, postId);
        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(savedComment);
    }

    @Override
    public Page<CommentResponseDto> findComments(Long postId, int page, int size) {
        // 최신 순으로 정렬되어 있고, 다른 정렬 기준(좋아요순)이 필요하면 RequestParam 으로 받을 수 있을 것 같다
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Comment> commentPage = commentRepository.findAllByPostId(pageable, postId);

        return commentPage.map(CommentResponseDto::new);
    }

    @Transactional
    @Override
    public CommentResponseDto updateComment(Long postId, Long commentId, String content, Long memberId) {
        Comment comment = commentRepository.findCommentByIdOrElseThrow(commentId);
        if(!memberId.equals(comment.getMemberId())){
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_ACTION);
        }

        comment.updateContent(content);

        return new CommentResponseDto(comment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId, Long memberId) {
        Comment comment = commentRepository.findCommentByIdOrElseThrow(commentId);
        if(!memberId.equals(comment.getMemberId())){
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_ACTION);
        }

        commentRepository.delete(comment);
    }
}
