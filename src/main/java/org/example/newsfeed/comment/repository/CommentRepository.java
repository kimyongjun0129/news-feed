package org.example.newsfeed.comment.repository;

import org.example.newsfeed.comment.entity.Comment;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    default Comment findCommentByIdOrElseThrow(Long id){
        return findById(id)
                .orElseThrow(() ->
                        new CustomException(CustomErrorCode.COMMENT_NOT_FOUND)
                );
    }

    Page<Comment> findAllByPostId(Pageable pageable, Long postId);

    boolean existsByIdAndPostId(Long commentId, Long postId);

    @Query("SELECT c.post.id, COUNT(c) FROM Comment c WHERE c.post.id IN :postIdList GROUP BY c.post.id")
    List<Object[]> countCommentsByPostIds(@Param("postIdList")List<Long> postIdList);
}
