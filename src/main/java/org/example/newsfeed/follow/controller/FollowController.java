package org.example.newsfeed.follow.controller;

import org.example.newsfeed.follow.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class FollowController {
    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<Void> followUser(
            @PathVariable("id") Long followerId,
            @RequestAttribute("memberId") Long followeeId
    ) {
        followService.followUser(followeeId, followerId);
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