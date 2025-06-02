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

import java.util.Optional;

import static org.example.newsfeed.common.exception.error.CustomErrorCode.*;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void followUser(Long followeeId, Long followerId) {

        // 팔로워 ID와 팔로이 ID가 같으면, 예외 발생
        if(followerId.equals(followeeId)) {
            throw new CustomException(SELF_FOLLOW_NOT_ALLOWED);
        }

        Optional<Member> followee = memberRepository.findMemberById(followeeId);
        Optional<Member> follower = memberRepository.findMemberById(followerId);

        // 팔로워가 존재하지 않는 경우, 예외 발생
        if(followee.isEmpty() || follower.isEmpty()) {
            throw new CustomException(USER_NOT_FOUND);
        }

        // 이미 팔로잉한 유저한테, 팔로우 건 경우 예외 발생
        if(followRepository.findFollowByFolloweeAndFollower(followee.get(), follower.get()).isPresent()) {
            throw new CustomException(FOLLOW_ALREADY_EXISTS);
        }

        Follow follows = new Follow(followee.get(), follower.get());

        followRepository.save(follows);
    }

    @Transactional
    public void unfollowUser(Long followeeId, Long unFollowerId) {

        // 본인을 언팔로우 하려는 경우, 예외 발생
        if(unFollowerId.equals(followeeId)) {
            throw new CustomException(SELF_UNFOLLOW_NOT_ALLOWED);
        }

        Optional<Member> unFollower = memberRepository.findMemberById(unFollowerId);

        // 언팔로우 하려는 사용자가 없는 경우, 에외 발생
        if (unFollower.isEmpty()) {
            throw new CustomException(USER_NOT_FOUND);
        }

        Optional<Follow> follow = followRepository.findFollowByFollower(unFollower.get());

        // 팔로인 되어있지 않는데 언팔로우 하려는 경우, 예외 발생
        if (follow.isEmpty()) {
            throw new CustomException(FOLLOW_NOT_FOUND);
        }

        followRepository.delete(follow.get());
    }
}

