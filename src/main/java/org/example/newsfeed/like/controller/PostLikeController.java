package org.example.newsfeed.like.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.dto.AuthUser;
import org.example.newsfeed.like.dto.LikeResponseDto;
import org.example.newsfeed.like.dto.PostOrCommentLikesResponseDto;
import org.example.newsfeed.like.repository.LikeRepository;
import org.example.newsfeed.like.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PostLikeController {

    private final LikeService likeService;

    /**
     * 좋아요 생성 메소드
     * 성공 시 201 CREATED
     * @param authUser
     * @param postId
     * @return
     */
    @PostMapping("/api/posts/{postId}/likes")
    public ResponseEntity<LikeResponseDto> like(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId){

        LikeResponseDto likeResponseDto = likeService.like(
                authUser.getId(), postId
        );

        return new ResponseEntity<>(likeResponseDto,HttpStatus.CREATED);
    }

    /**
     * 좋아요 취소 메소드
     * 성공 시 200 OK
     * @param authUser
     * @param postId
     */
    @DeleteMapping("/api/posts/{postId}/likes")
    public void unlike(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId){

        likeService.unlike(authUser.getId(), postId);
    }

    /**
     * 게시물을 좋아요 한 memberId 리스트,
     * 게시물 좋아요 개수,
     * 로그인한 사용자가 좋아요 했는지
     * PostLikesResponseDto 에 넣어 반환
     *
     * @param authUser
     * @param postId
     * @return
     */
    @GetMapping("/api/posts/{postId}/likes")
    public ResponseEntity<PostOrCommentLikesResponseDto> countByPostId(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId){

        PostOrCommentLikesResponseDto postLikesResponseDto = likeService.findByPostId(authUser.getId(), postId);

        return new ResponseEntity<>(postLikesResponseDto, HttpStatus.OK);
    }

    /**
     * memberId 해당 사용자가
     * 좋아요 한 게시물 리스트 반환
     * @param memberId
     * @return
     */
    @GetMapping("/api/members/{memberId}/likes")
    public ResponseEntity<List<LikeResponseDto>> findByMemberId(@PathVariable Long memberId){

        List<LikeResponseDto> likeResponseDtoList =
                likeService.findByMemberId(memberId);

        return new ResponseEntity<>(likeResponseDtoList, HttpStatus.OK);
    }

}
