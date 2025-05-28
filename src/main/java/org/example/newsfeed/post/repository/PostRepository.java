package org.example.newsfeed.post.repository;

import org.example.newsfeed.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 적합한 exception 으로 변경하기
    default Post findPostByIdOrElseThrow(Long id){
        return findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Post not found with id " + id
                        )
                );
    }
}
