package org.example.newsfeed.like.repository;

import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findByMemberId(Long memberId);
    List<Like> findByPostId(Long postId);
    Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId);
    int countByPostId(Long postId);

    default Like findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(()
                -> new CustomException(CustomErrorCode.LIKE_NOT_FOUND));
    }

    default Like findByMemberIdAndPostIdOrElseThrow(Long memberId, Long postId){
        return findByMemberIdAndPostId(memberId, postId).orElseThrow(()
                -> new CustomException(CustomErrorCode.LIKE_NOT_FOUND));
    }

}
