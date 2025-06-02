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

        // 팔로워 ID와 팔로이 ID가 같으면, 예외 발생
        if(followerId.equals(followeeId)) {
            throw new CustomException(CustomErrorCode.SELF_FOLLOW_NOT_ALLOWED);
        }

        Member followee = memberRepository.findMemberByIdOrElseThrow(followeeId);
        Member follower = memberRepository.findMemberByIdOrElseThrow(followerId);

        // 이미 팔로잉한 유저한테, 팔로우 건 경우 예외 발생
        if(followRepository.findFollowByFolloweeAndFollower(followee, follower).isPresent()) {
            throw new CustomException(CustomErrorCode.FOLLOW_ALREADY_EXISTS);
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

