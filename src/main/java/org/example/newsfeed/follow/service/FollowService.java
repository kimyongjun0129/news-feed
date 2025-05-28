package org.example.newsfeed.follow.service;

import org.example.newsfeed.follow.entity.Follow;
import org.example.newsfeed.follow.repository.FollowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    @Transactional
    public void followUser(Long id) {

        Follow follows = new Follow(2L, id);

        followRepository.save(follows);
    }

    @Transactional
    public void unfollowUser(Long id) {

        List<Follow> byFollowerId = followRepository.findByFolloweeId(id);
        Follow follow = byFollowerId.get(0);

        followRepository.delete(follow);
    }
}
