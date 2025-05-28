package org.example.newsfeed.post.repository;

import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 적합한 exception 으로 변경하기
    default Post findPostByIdOrElseThrow(Long id){
        return findById(id)
                .orElseThrow(() ->
                        new CustomException(CustomErrorCode.POST_NOT_FOUND)
                );
    }

    List<Post> findAllByUpdatedAtBetween(LocalDateTime updatedAtAfter, LocalDateTime updatedAtBefore);
}
