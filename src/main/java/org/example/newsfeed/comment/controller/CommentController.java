package org.example.newsfeed.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.comment.dto.CommentRequestDto;
import org.example.newsfeed.comment.dto.CommentResponseDto;
import org.example.newsfeed.comment.service.CommentService;
import org.example.newsfeed.common.dto.AuthUser;
import org.example.newsfeed.post.dto.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // (PostId 게시글에) 댓글 생성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDto dto,
            @AuthenticationPrincipal AuthUser authUser
    ){
        CommentResponseDto responseDto = commentService.createComment(
                postId,
                dto.getContent(),
                authUser.getId()
        );
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // (PostId 게시물에 포함된) 댓글 조회
    @GetMapping
    public ResponseEntity<PageDto<CommentResponseDto>> findComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size
    ){
        Page<CommentResponseDto> responseDtoPage = commentService.findComments(postId, page, size);
        return new ResponseEntity<>(new PageDto<>(responseDtoPage), HttpStatus.OK);
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto dto,
            @AuthenticationPrincipal AuthUser authUser
    ){
        CommentResponseDto responseDto = commentService.updateComment(
                postId,
                commentId,
                dto.getContent(),
                authUser.getId()
        );
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal AuthUser authUser
    ){
        commentService.deleteComment(postId, commentId, authUser.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}