package org.example.newsfeed.like.repository;

import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.like.entity.CommentLike;
import org.example.newsfeed.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    List<CommentLike> findByMemberId(Long memberId);
    List<CommentLike> findMemberIdByCommentId(Long commentId);
    Optional<CommentLike> findByMemberIdAndCommentId(Long memberId, Long commentId);
    Long countByCommentId(Long commentId);
    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);

    default CommentLike findByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(()
                -> new CustomException(CustomErrorCode.LIKE_NOT_FOUND));
    }

    default CommentLike findByMemberIdAndCommentIdOrElseThrow(Long memberId, Long commentId){
        return findByMemberIdAndCommentId(memberId, commentId).orElseThrow(()
                -> new CustomException(CustomErrorCode.LIKE_NOT_FOUND));
    }

    @Query("SELECT cl.commentId, count(*) FROM CommentLike cl WHERE cl.commentId IN :commentIdList GROUP BY cl.commentId")
    List<Object[]> countLikesByCommentIds(@Param("commentIdList")List<Long> commentIdList);
}
