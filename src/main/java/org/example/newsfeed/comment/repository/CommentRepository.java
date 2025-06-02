package org.example.newsfeed.comment.repository;

import org.example.newsfeed.comment.entity.Comment;
import org.example.newsfeed.comment.repository.projection.CommentCount;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // postId 로 게시글 댓글 개수 찾기
    Long countByPostId(Long id);

    // commentId 로 comment 찾기
    default Comment findCommentByIdOrElseThrow(Long id){
        return findById(id)
                .orElseThrow(() ->
                        new CustomException(CustomErrorCode.COMMENT_NOT_FOUND)
                );
    }

    // pageable 설정(offset, size, 정렬)에 해당하는 comment 들을 Page 의 content 에 넣어서 리턴
    Page<Comment> findAllByPostId(Pageable pageable, Long postId);

    boolean existsByIdAndPostId(Long commentId, Long postId);

    // postIdList 에 해당하는 postId 별 comment 개수
    @Query("SELECT c.post.id AS postId, COUNT(c) AS COUNT FROM Comment c WHERE c.post.id IN :postIdList GROUP BY c.post.id")
    List<CommentCount> countCommentsByPostIds(@Param("postIdList")List<Long> postIdList);
}
