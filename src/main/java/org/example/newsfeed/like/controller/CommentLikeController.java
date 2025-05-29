package org.example.newsfeed.like.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.like.dto.*;
import org.example.newsfeed.like.repository.CommentLikeRepository;
import org.example.newsfeed.like.repository.LikeRepository;
import org.example.newsfeed.like.service.CommentLikeService;
import org.example.newsfeed.like.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;
    private final CommentLikeRepository commentLikeRepository;

    @PostMapping("/api/comments/{commentId}/likes")
    public ResponseEntity<CommentLikeResponseDto> like(
            @RequestBody CommentLikeRequestDto requestDto, //세션 로그인 아이디로 변경
            @PathVariable Long commentId){

        CommentLikeResponseDto commentLikeResponseDto = commentLikeService.like(requestDto.getMemberId(), commentId);

        return new ResponseEntity<>(commentLikeResponseDto,HttpStatus.CREATED);
    }

    @DeleteMapping("/api/comments/{commentId}/likes")
    public void unlike(
            @RequestBody CommentLikeRequestDto requestDto, //세션 로그인 아이디로 변경
            @PathVariable Long commentId){

        commentLikeService.unlike(requestDto.getMemberId(), commentId);
    }

    /**
     * commentId/likes get 매핑 시
     * 댓글을 좋아요 한 memberId 리스트,
     * 댓글 좋아요 개수,
     * 로그인한 사용자가 좋아요 했는지
     * PostOrCommentLikesResponseDto 에 넣어 반환
     *
     * @param requestDto
     * @param commentId
     * @return
     */
    @GetMapping("/api/posts/{commentId}/likes")
    public ResponseEntity<PostOrCommentLikesResponseDto> findByCommentId(
            @RequestBody LikeRequestDto requestDto, //세션 로그인 아이디로 변경
            @PathVariable Long commentId){

        PostOrCommentLikesResponseDto commentLikesResponseDto = commentLikeService.findByCommentId(requestDto.getMemberId(), commentId);

        return new ResponseEntity<>(commentLikesResponseDto, HttpStatus.OK);
    }

}
