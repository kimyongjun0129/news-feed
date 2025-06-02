package org.example.newsfeed.like.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.dto.AuthUser;
import org.example.newsfeed.like.dto.LikeResponseDto;
import org.example.newsfeed.like.dto.PostOrCommentLikesResponseDto;
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
     * 게시물에 좋아요를 생성합니다.
     * ---
     * 로그인한 사용자가 지정한 게시물에 좋아요를 누릅니다.
     * 성공적으로 처리되면 HTTP 201(CREATED) 상태와 함께 좋아요 정보가 반환됩니다.
     *
     * @param authUser  인증된 사용자 정보
     * @param postId    좋아요할 게시물 ID
     * @return          생성된 좋아요 정보를 담은 DTO
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
     * 게시물에 대한 좋아요를 취소합니다.
     * ---
     * 로그인한 사용자가 지정한 게시물에 누른 좋아요를 취소합니다.
     * 성공 시 HTTP 200(OK)를 반환합니다.
     *
     * @param authUser  인증된 사용자 정보
     * @param postId    좋아요를 취소할 게시물 ID
     */
    @DeleteMapping("/api/posts/{postId}/likes")
    public void unlike(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId){

        likeService.unlike(authUser.getId(), postId);
    }

    /**
     * 게시물의 좋아요 정보를 조회합니다.
     * ---
     * 다음 정보를 포함한 DTO를 반환합니다:
     * - 해당 게시물을 좋아요한 사용자 ID 리스트
     * - 좋아요 총 개수
     * - 로그인한 사용자가 해당 게시물을 좋아요했는지 여부
     *
     * @param authUser  인증된 사용자 정보
     * @param postId    조회할 게시물 ID
     * @return          게시물의 좋아요 정보가 담긴 DTO
     */
    @GetMapping("/api/posts/{postId}/likes")
    public ResponseEntity<PostOrCommentLikesResponseDto> findByPostId(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long postId){

        PostOrCommentLikesResponseDto postLikesResponseDto = likeService.findByPostId(authUser.getId(), postId);

        return new ResponseEntity<>(postLikesResponseDto, HttpStatus.OK);
    }

    /**
     * 특정 사용자가 좋아요한 게시물 목록을 조회합니다.
     * ---
     * 지정한 사용자 ID 기준으로, 사용자가 좋아요한 게시물들의 리스트를 반환합니다.
     *
     * @param memberId  좋아요한 게시물을 조회할 사용자 ID
     * @return          사용자가 좋아요한 게시물들의 리스트
     */
    @GetMapping("/api/members/{memberId}/likes")
    public ResponseEntity<List<LikeResponseDto>> findByMemberId(@PathVariable Long memberId){

        List<LikeResponseDto> likeResponseDtoList =
                likeService.findByMemberId(memberId);

        return new ResponseEntity<>(likeResponseDtoList, HttpStatus.OK);
    }
}
