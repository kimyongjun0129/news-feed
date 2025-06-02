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

    /**
     * 팔로이(followeeId)가 팔로워(followerId)를 팔로우하도록 처리합니다.
     * <p>
     * 이 메서드는 다음과 같은 검증을 수행합니다:
     * <ul>
     *   <li>자기 자신을 팔로우하려는 경우 예외를 발생시킵니다.</li>
     *   <li>팔로워 또는 팔로이 유저가 존재하지 않는 경우 예외를 발생시킵니다.</li>
     *   <li>이미 팔로우한 유저를 다시 팔로우하려는 경우 예외를 발생시킵니다.</li>
     * </ul>
     * 모든 검증을 통과하면 새로운 {@link Follow} 엔티티를 생성하여 저장합니다.
     * </p>
     *
     * @param followeeId 팔로우 당할 유저의 ID
     * @param followerId 팔로우를 시도하는 유저의 ID
     * @throws CustomException 다음과 같은 경우 예외가 발생합니다:
     *                         <ul>
     *                           <li>자기 자신을 팔로우하려는 경우 (SELF_FOLLOW_NOT_ALLOWED)</li>
     *                           <li>유저가 존재하지 않는 경우 (USER_NOT_FOUND)</li>
     *                           <li>이미 팔로우 관계가 존재하는 경우 (FOLLOW_ALREADY_EXISTS)</li>
     *                         </ul>
     */
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

    /**
     * 사용자가 팔로우하고 있던 유저를 언팔로우 처리합니다.
     *
     * <p>
     * 이 메서드는 다음과 같은 검증을 수행합니다:
     * <ul>
     *   <li>자기 자신을 언팔로우하려는 경우 예외 발생</li>
     *   <li>언팔로우 시도하는 사용자가 존재하지 않을 경우 예외 발생</li>
     *   <li>실제로 팔로우 관계가 존재하지 않을 경우 예외 발생</li>
     * </ul>
     * 검증을 통과하면 {@link Follow} 엔티티를 삭제하여 언팔로우 처리를 완료합니다.
     * </p>
     *
     * @param followeeId 언팔로우 당할 유저의 ID
     * @param unFollowerId 언팔로우를 시도하는 유저의 ID
     * @throws CustomException 다음의 경우 예외 발생:
     * <ul>
     *   <li>자기 자신을 언팔로우하려는 경우 (SELF_UNFOLLOW_NOT_ALLOWED)</li>
     *   <li>언팔로우를 시도하는 유저가 존재하지 않을 경우 (USER_NOT_FOUND)</li>
     *   <li>팔로우 관계가 존재하지 않을 경우 (FOLLOW_NOT_FOUND)</li>
     * </ul>
     */
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

