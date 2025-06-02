package org.example.newsfeed.like.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.comment.entity.Comment;
import org.example.newsfeed.comment.repository.CommentRepository;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.like.dto.CommentLikeResponseDto;
import org.example.newsfeed.like.dto.PostOrCommentLikesResponseDto;
import org.example.newsfeed.like.entity.CommentLike;
import org.example.newsfeed.like.repository.CommentLikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;

    /**
     * 좋아요 생성
     */
    public CommentLikeResponseDto like(Long memberId, Long postId, Long commentId){
        Comment comment = commentRepository.findCommentByIdOrElseThrow(commentId);

        // 본인 댓글에 좋아요 하려할 때
        if(comment.getMember().getId().equals(memberId)){
            throw new CustomException(CustomErrorCode.CANNOT_LIKE_OWN_COMMENT);
        }

        // 이미 좋아요 한 댓글에 좋아요 하려할 때
        if(commentLikeRepository.findByMemberIdAndCommentId(memberId, commentId).isPresent()){
            throw new CustomException(CustomErrorCode.LIKE_ALREADY_EXISTS);
        }

        // 해당 게시물에 달린 댓글이 아닐 때
        if(!commentRepository.existsByIdAndPostId(commentId, postId)){
            throw new CustomException(CustomErrorCode.COMMENT_NOT_BELONG_TO_POST);
        }

        CommentLike commentLike = new CommentLike(memberId, commentId);
        commentLikeRepository.save(commentLike);

        return new CommentLikeResponseDto(commentLike.getId(), commentLike.getMemberId(), commentLike.getCommentId());
    }

    /**
     * 좋아요 취소
     */
    @Transactional
    public void unlike(Long memberId, Long commentId){
        CommentLike commentLike = commentLikeRepository.findByMemberIdAndCommentIdOrElseThrow(memberId, commentId);
        commentLikeRepository.delete(commentLike);
    }

    /**
     * 게시물 id로 게시물에 달린 좋아요 조회
     */
    public PostOrCommentLikesResponseDto findByCommentId(Long memberId, Long commentId){

        // 댓글이 존재하지 않을 때
        Comment comment = commentRepository.findCommentByIdOrElseThrow(commentId);

        List<CommentLike> likes = commentLikeRepository.findMemberIdByCommentId(commentId);
        List<Long> memberIds = likes.stream().map(CommentLike::getMemberId).toList();
        Long countLikes = commentLikeRepository.countByCommentId(commentId);
        boolean likedByMe = commentLikeRepository.existsByMemberIdAndCommentId(memberId, commentId);

        return new PostOrCommentLikesResponseDto(memberIds, countLikes, likedByMe);

    }

}
