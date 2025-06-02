package org.example.newsfeed.like.repository;

import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.like.entity.Like;
import org.example.newsfeed.like.repository.projection.PostLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findByMemberId(Long memberId);
    List<Like> findMemberIdByPostId(Long postId);
    Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId);
    Long countByPostId(Long postId);
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    default Like findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(()
                -> new CustomException(CustomErrorCode.LIKE_NOT_FOUND));
    }

    default Like findByMemberIdAndPostIdOrElseThrow(Long memberId, Long postId){
        return findByMemberIdAndPostId(memberId, postId).orElseThrow(()
                -> new CustomException(CustomErrorCode.LIKE_NOT_FOUND));
    }

    // postIdList 에 해당하는 postId 별 like 개수
    @Query("SELECT l.postId AS postId, COUNT(l) AS COUNT FROM Like l WHERE l.postId IN :postIdList GROUP BY l.postId")
    List<PostLikeCount> countLikesByPostIds(@Param("postIdList") List<Long> postIdList);
}
