package org.example.newsfeed.follow.repository;

import org.example.newsfeed.follow.entity.Follow;
import org.example.newsfeed.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findFollowByFolloweeAndFollower(Member followee, Member follower);

    Optional<Follow> findFollowByFollower(Member follower);

    void deleteByFolloweeId(Long memberId);
}
