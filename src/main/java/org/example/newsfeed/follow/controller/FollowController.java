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

    @PostMapping("/{id}/follow")
    public ResponseEntity<Void> followUser(
            @PathVariable("id") Long followerId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        followService.followUser(authUser.getId(), followerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/unfollow")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable("id") Long id
    ) {
        followService.unfollowUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}