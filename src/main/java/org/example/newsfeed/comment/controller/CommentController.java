package org.example.newsfeed.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.comment.dto.CommentRequestDto;
import org.example.newsfeed.comment.dto.CommentResponseDto;
import org.example.newsfeed.comment.service.CommentService;
import org.example.newsfeed.post.dto.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDto dto
            //,@SessionAttribute(Const.LOGIN_USER) UserResponseDto user
    ){
        CommentResponseDto responseDto = commentService.createComment(
                postId,
                dto.getContent(),
                1L
        );
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageDto<CommentResponseDto>> findComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size
            //,@SessionAttribute(Const.LOGIN_USER) UserResponseDto user
    ){
        Page<CommentResponseDto> responseDtoPage = commentService.findComments(postId, page, size);
        return new ResponseEntity<>(new PageDto<>(responseDtoPage), HttpStatus.OK);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto dto
            //,@SessionAttribute(Const.LOGIN_USER) UserResponseDto user
    ){
        CommentResponseDto responseDto = commentService.updateComment(
                postId,
                commentId,
                dto.getContent(),
                1L
        );
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId
            //,@SessionAttribute(Const.LOGIN_USER) UserResponseDto user
    ){
        commentService.deleteComment(postId, commentId, 1L);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}