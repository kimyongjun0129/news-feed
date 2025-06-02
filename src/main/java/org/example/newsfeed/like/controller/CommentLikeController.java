package org.example.newsfeed.like.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.like.dto.*;
import org.example.newsfeed.like.service.CommentLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    /**
     * 좋아요 생성 메소드
     * 성공 시 201 CREATED
     * @param memberId
     * @param postId
     * @param commentId
     * @return
     */
    @PostMapping("/api/posts/{postId}/comments/{commentId}/likes")
    public ResponseEntity<CommentLikeResponseDto> like(
            @RequestAttribute("memberId") Long memberId,
            @PathVariable Long postId,
            @PathVariable Long commentId){

        CommentLikeResponseDto commentLikeResponseDto = commentLikeService.like(memberId, postId, commentId);

        return new ResponseEntity<>(commentLikeResponseDto,HttpStatus.CREATED);
    }

    /**
     * 좋아요 취소 메소드
     * 성공 시 200 OK
     * @param memberId
     * @param commentId
     */
    @DeleteMapping("/api/posts/{postId}/comments/{commentId}/likes")
    public void unlike(
            @RequestAttribute("memberId") Long memberId,
            @PathVariable Long commentId){

        commentLikeService.unlike(memberId, commentId);
    }

    /**
     * 댓글을 좋아요 한 memberId 리스트,
     * 댓글 좋아요 개수,
     * 로그인한 사용자가 좋아요 했는지
     * PostOrCommentLikesResponseDto 에 넣어 반환
     *
     * @param memberId
     * @param commentId
     * @return
     */
    @GetMapping("/api/posts/{postId}/comments/{commentId}/likes")
    public ResponseEntity<PostOrCommentLikesResponseDto> findByCommentId(
            @RequestAttribute("memberId") Long memberId,
            @PathVariable Long commentId){

        PostOrCommentLikesResponseDto commentLikesResponseDto = commentLikeService.findByCommentId(memberId, commentId);

        return new ResponseEntity<>(commentLikesResponseDto, HttpStatus.OK);
    }

}
