package org.example.newsfeed.like.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.dto.AuthUser;
import org.example.newsfeed.like.dto.*;
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

    /**
     * 특정 댓글에 좋아요를 추가합니다.
     * ---
     * 사용자가 지정한 게시글 내 댓글에 대해 좋아요를 누르면, 새로운 좋아요가 생성됩니다.
     * 성공 시 HTTP 상태 코드 201 (CREATED)를 반환합니다.
     *
     * @param authUser  인증된 사용자 정보
     * @param postId    좋아요를 추가할 게시글 ID
     * @param commentId 좋아요를 추가할 댓글 ID
     * @return          생성된 댓글 좋아요 정보를 담은 응답 DTO
     */
    @PostMapping("/api/posts/{postId}/comments/{commentId}/likes")
    public ResponseEntity<CommentLikeResponseDto> like(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId,
            @PathVariable Long commentId){

        CommentLikeResponseDto commentLikeResponseDto = commentLikeService.like(authUser.getId(), postId, commentId);

        return new ResponseEntity<>(commentLikeResponseDto,HttpStatus.CREATED);
    }

    /**
     * 특정 댓글에 대한 좋아요를 취소합니다.
     * ---
     * 인증된 사용자가 지정한 댓글에 대해 이전에 눌렀던 좋아요를 취소합니다.
     * 성공적으로 취소될 경우 HTTP 200 OK 상태를 반환합니다.
     *
     * @param authUser  인증된 사용자 정보
     * @param commentId 좋아요를 취소할 댓글 ID
     *
     */
    @DeleteMapping("/api/posts/{postId}/comments/{commentId}/likes")
    public void unlike(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long commentId){

        commentLikeService.unlike(authUser.getId(), commentId);
    }

    /**
     * 특정 댓글의 좋아요 정보를 조회합니다.
     * ---
     * 이 메서드는 다음 정보를 포함한 DTO를 반환합니다:
     * - 해당 댓글에 좋아요를 누른 사용자 ID 리스트
     * - 전체 좋아요 개수
     * - 현재 로그인한 사용자가 해당 댓글에 좋아요를 눌렀는지 여부
     *
     * @param authUser  인증된 사용자 정보
     * @param commentId 조회할 댓글의 ID
     * @return          댓글에 대한 좋아요 정보가 담긴 PostOrCommentLikesResponseDto
     */
    @GetMapping("/api/posts/{postId}/comments/{commentId}/likes")
    public ResponseEntity<PostOrCommentLikesResponseDto> findByCommentId(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long commentId){

        PostOrCommentLikesResponseDto commentLikesResponseDto = commentLikeService.findByCommentId(authUser.getId(), commentId);

        return new ResponseEntity<>(commentLikesResponseDto, HttpStatus.OK);
    }

}
