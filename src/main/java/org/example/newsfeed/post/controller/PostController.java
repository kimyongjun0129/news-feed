package org.example.newsfeed.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.post.dto.PageDto;
import org.example.newsfeed.post.dto.PostCreateRequestDto;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.dto.PostUpdateRequestDto;
import org.example.newsfeed.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @RequestBody PostCreateRequestDto dto
    ){
        PostResponseDto responseDto = postService.createPost(
                dto.getTitle(),
                dto.getContent()
        );
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageDto<PostResponseDto>> findPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<PostResponseDto> responseDtoPage = postService.findPosts(page, size);
        return new ResponseEntity<>(new PageDto<>(responseDtoPage), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long id,
            @RequestBody PostUpdateRequestDto dto
    ){
        PostResponseDto responseDto = postService.updatePost(
                id,
                dto.getTitle(),
                dto.getContent(),
                1L
        );
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id, 1L);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}