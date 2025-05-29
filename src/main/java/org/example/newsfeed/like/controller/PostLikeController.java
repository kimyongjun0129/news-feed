package org.example.newsfeed.like.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.like.dto.LikeRequestDto;
import org.example.newsfeed.like.dto.LikeResponseDto;
import org.example.newsfeed.like.dto.PostOrCommentLikesResponseDto;
import org.example.newsfeed.like.repository.LikeRepository;
import org.example.newsfeed.like.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PostLikeController {

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

    /**
     * postId/likes get 매핑 시
     * 게시물을 좋아요 한 memberId 리스트,
     * 게시물 좋아요 개수,
     * 로그인한 사용자가 좋아요 했는지
     * PostLikesResponseDto 에 넣어 반환
     *
     * @param requestDto
     * @param postId
     * @return
     */
    @GetMapping("/api/posts/{postId}/likes")
    public ResponseEntity<PostOrCommentLikesResponseDto> countByPostId(
            @RequestBody LikeRequestDto requestDto, //세션 로그인 아이디로 변경
            @PathVariable Long postId){

        PostOrCommentLikesResponseDto postLikesResponseDto = likeService.findByPostId(requestDto.getMemberId(), postId);

        return new ResponseEntity<>(postLikesResponseDto, HttpStatus.OK);
    }

    @GetMapping("/api/members/{memberId}/likes")
    public ResponseEntity<List<LikeResponseDto>> findByMemberId(@PathVariable Long memberId){

        List<LikeResponseDto> likeResponseDtoList =
                likeService.findByMemberId(memberId);

        return new ResponseEntity<>(likeResponseDtoList, HttpStatus.OK);
    }

}
