package org.example.newsfeed.like.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.like.dto.LikeCountResponseDto;
import org.example.newsfeed.like.dto.LikeRequestDto;
import org.example.newsfeed.like.dto.LikeResponseDto;
import org.example.newsfeed.like.repository.LikeRepository;
import org.example.newsfeed.like.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final LikeRepository likeRepository;

    @PostMapping("/api/posts/{postId}/likes")
    public ResponseEntity<LikeResponseDto> like(
            @RequestBody LikeRequestDto requestDto, //세션 로그인 아이디로 변경
            @PathVariable Long postId){

        LikeResponseDto likeResponseDto = likeService.like(requestDto.getMemberId(), requestDto.getPostId());

        return new ResponseEntity<>(likeResponseDto,HttpStatus.CREATED);
    }

    @DeleteMapping("/api/posts/{postId}/likes")
    public void unlike(
            @RequestBody LikeRequestDto requestDto, //세션 로그인 아이디로 변경
            @PathVariable Long postId){

        likeService.unlike(requestDto.getMemberId(), postId);
    }

    @GetMapping("/api/posts/{postId}/likes")
    public ResponseEntity<LikeCountResponseDto> countByPostId(@PathVariable Long postId){

        LikeCountResponseDto likeCountResponseDto = likeService.countByPostId(postId);
        return new ResponseEntity<LikeCountResponseDto>(likeCountResponseDto, HttpStatus.OK);
    }

    @GetMapping("/api/members/{memberId}/likes")
    public ResponseEntity<List<LikeResponseDto>> findBymemberId(@PathVariable Long memberId){

        List<LikeResponseDto> likeResponseDtoList =
                likeService.findByMemberId(memberId);

        return new ResponseEntity<>(likeResponseDtoList, HttpStatus.OK);
    }

}
