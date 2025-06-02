package org.example.newsfeed.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.dto.AuthUser;
import org.example.newsfeed.post.dto.PageDto;
import org.example.newsfeed.post.dto.PostCreateRequestDto;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.dto.PostUpdateRequestDto;
import org.example.newsfeed.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 게시물 생성
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @Valid @RequestBody PostCreateRequestDto dto,
            @AuthenticationPrincipal AuthUser authUser
    ){
        PostResponseDto responseDto = postService.createPost(
                dto.getTitle(),
                dto.getContent(),
                authUser.getId()
        );
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 단일 게시물 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(
            @PathVariable Long postId
    ) {
        return new ResponseEntity<>(postService.getPost(postId), HttpStatus.OK);
    }

    // 게시물 페이지 조회
    @GetMapping
    public ResponseEntity<PageDto<PostResponseDto>> findPosts(
            @RequestParam(defaultValue = "2025-05-28-00-00-00") @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")LocalDateTime from,
            @RequestParam(defaultValue = "2025-05-30-23-59-59") @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss")LocalDateTime to,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<PostResponseDto> responseDtoPage = postService.findPosts(from, to, page, size);
        return new ResponseEntity<>(new PageDto<>(responseDtoPage), HttpStatus.OK);
    }

    // 게시물 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequestDto dto,
            @AuthenticationPrincipal AuthUser authUser
    ){
        PostResponseDto responseDto = postService.updatePost(
                postId,
                dto.getTitle(),
                dto.getContent(),
                authUser.getId()
        );
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 게시물 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        postService.deletePost(postId, authUser.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}