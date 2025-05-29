package org.example.newsfeed.follow.repository;

import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.follow.entity.Follow;
import org.example.newsfeed.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // Follow 반환, 없다면 USER_NOT_FOUND 예외 발생
    default Follow findFollowByIdOrElseThrow(Long id){
        return findById(id)
                .orElseThrow(() ->
                        new CustomException(CustomErrorCode.USER_NOT_FOUND)
                );
    }

    Optional<Follow> findFollowByFolloweeAndFollower(Member followee, Member follower);
}
