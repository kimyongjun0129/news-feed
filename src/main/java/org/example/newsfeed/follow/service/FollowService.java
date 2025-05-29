package org.example.newsfeed.follow.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.follow.entity.Follow;
import org.example.newsfeed.follow.repository.FollowRepository;
import org.example.newsfeed.member.entity.Member;
import org.example.newsfeed.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void followUser(Long followeeId, Long followerId) {

        if(followerId.equals(followeeId)) {
            throw new CustomException(CustomErrorCode.USER_NOT_FOUND);
        }

        Member followee = memberRepository.findMemberByIdOrElseThrow(followeeId);
        Member follower = memberRepository.findMemberByIdOrElseThrow(followerId);

        if(followRepository.findFollowByFolloweeAndFollower(followee, follower).isPresent()) {
            throw new CustomException(CustomErrorCode.USER_NOT_FOUND);
        }

        Follow follows = new Follow(followee, follower);

        followRepository.save(follows);
    }

    @Transactional
    public void unfollowUser(Long followerId) {

        Follow follow = followRepository.findFollowByIdOrElseThrow(followerId);

        followRepository.delete(follow);
    }
}
