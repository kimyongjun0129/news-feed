package org.example.newsfeed.follow.repository;

import org.example.newsfeed.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFolloweeId(Long followeeId);
}
