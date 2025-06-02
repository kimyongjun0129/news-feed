package org.example.newsfeed.post.repository;

import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PostRepository extends JpaRepository<Post, Long> {
    // memberId로 게시물 지우기
    void deleteByMemberId(Long memberId);

    // postId로 게시물 찾기
    default Post findPostByIdOrElseThrow(Long id){
        return findById(id)
                .orElseThrow(() ->
                        new CustomException(CustomErrorCode.POST_NOT_FOUND)
                );
    }

    Page<Post> findAllByUpdatedAtBetween(LocalDateTime updatedAtAfter, LocalDateTime updatedAtBefore, Pageable pageable);
}
