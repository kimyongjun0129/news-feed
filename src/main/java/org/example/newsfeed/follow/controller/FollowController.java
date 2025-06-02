package org.example.newsfeed.follow.controller;

import org.example.newsfeed.common.dto.AuthUser;
import org.example.newsfeed.follow.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class FollowController {
    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    /**
     * @author : kimyongjun0129
     * @param   followerId   팔로우 걸고 싶은 사람의 PK 값
     * @param   authUser     현재 로그인된 사용자 객체 (Spring Security)
     * @return               상태코드 반환
     */
    @PostMapping("/{id}/follow")
    public ResponseEntity<Void> followUser(
            @PathVariable("id") Long followerId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        followService.followUser(authUser.getId(), followerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @author : kimyongjun0129
     * @param   unFollowerId    팔로우 걸고 싶은 사람의 PK 값
     * @return                  상태코드 반환
     */
    @PostMapping("/{id}/unfollow")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable("id") Long unFollowerId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        followService.unfollowUser(authUser.getId(), unFollowerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}