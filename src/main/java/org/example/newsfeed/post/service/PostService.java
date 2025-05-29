package org.example.newsfeed.post.service;

import org.example.newsfeed.post.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

public interface PostService {

    PostResponseDto createPost(String title, String content);

    PostResponseDto getPost(Long id);

    Page<PostResponseDto> findPosts(LocalDateTime from, LocalDateTime to, int page, int size);

    PostResponseDto updatePost(Long id, String title, String content, Long userId);

    void deletePost(Long id, Long userId);
}