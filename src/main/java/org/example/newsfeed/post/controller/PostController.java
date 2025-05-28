package org.example.newsfeed.post.controller;

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
            //,@SessionAttribute(Const.LOGIN_USER) UserResponseDto user
    ){
        PostResponseDto responseDto = postService.createPost(
                dto.getTitle(),
                dto.getContent()
        );
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(
            @PathVariable Long postId
            //,@SessionAttribute(Const.LOGIN_USER) UserResponseDto user
    ) {

        return new ResponseEntity<>(postService.getPost(postId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageDto<PostResponseDto>> findPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
            //,@SessionAttribute(Const.LOGIN_USER) UserResponseDto user
    ){
        Page<PostResponseDto> responseDtoPage = postService.findPosts(page, size);
        return new ResponseEntity<>(new PageDto<>(responseDtoPage), HttpStatus.OK);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequestDto dto
            //,@SessionAttribute(Const.LOGIN_USER) UserResponseDto user
    ){
        PostResponseDto responseDto = postService.updatePost(
                postId,
                dto.getTitle(),
                dto.getContent(),
                1L
        );
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId
            //,@SessionAttribute(Const.LOGIN_USER) UserResponseDto user
    ) {
        postService.deletePost(postId, 1L);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}