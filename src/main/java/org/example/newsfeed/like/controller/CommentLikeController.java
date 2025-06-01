package org.example.newsfeed.like.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.dto.AuthUser;
import org.example.newsfeed.like.dto.*;
import org.example.newsfeed.like.repository.CommentLikeRepository;
import org.example.newsfeed.like.service.CommentLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;
    private final CommentLikeRepository commentLikeRepository;

    @PostMapping("/api/posts/{postId}/comments/{commentId}/likes")
    public ResponseEntity<CommentLikeResponseDto> like(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId,
            @PathVariable Long commentId){

        CommentLikeResponseDto commentLikeResponseDto = commentLikeService.like(authUser.getId(), postId, commentId);

        return new ResponseEntity<>(commentLikeResponseDto,HttpStatus.CREATED);
    }

    @DeleteMapping("/api/posts/{postId}/comments/{commentId}/likes")
    public void unlike(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long commentId){

        commentLikeService.unlike(authUser.getId(), commentId);
    }

    /**
     * commentId/likes get 매핑 시
     * 댓글을 좋아요 한 memberId 리스트,
     * 댓글 좋아요 개수,
     * 로그인한 사용자가 좋아요 했는지
     * PostOrCommentLikesResponseDto 에 넣어 반환
     *
     * @param
     * @param commentId
     * @return
     */
    @GetMapping("/api/posts/{postId}/comments/{commentId}/likes")
    public ResponseEntity<PostOrCommentLikesResponseDto> findByCommentId(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long commentId){

        PostOrCommentLikesResponseDto commentLikesResponseDto = commentLikeService.findByCommentId(authUser.getId(), commentId);

        return new ResponseEntity<>(commentLikesResponseDto, HttpStatus.OK);
    }

}
