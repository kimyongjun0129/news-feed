package org.example.newsfeed.post.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.entity.Post;
import org.example.newsfeed.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public PostResponseDto createPost(String title, String content, Long memberId) {

        Post post = new Post(title, content, memberId);

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost);
    }

    @Override
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findPostByIdOrElseThrow(id);
        return new PostResponseDto(post);
    }

    @Override
    public Page<PostResponseDto> findPosts(LocalDateTime from, LocalDateTime to, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Post> postPage = postRepository.findAllByUpdatedAtBetween(from, to, pageable);

        return postPage.map(PostResponseDto::new);
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long id, String title, String content, Long userId) {
        Post post = postRepository.findPostByIdOrElseThrow(id);

        checkPostMemberId(post, userId);

        if(title != null){
            post.updateTitle(title);
        }
        if(content != null){
            post.updateContent(content);
        }

        return new PostResponseDto(post);
    }

    @Override
    @Transactional
    public void deletePost(Long id, Long userId) {
        Post post = postRepository.findPostByIdOrElseThrow(id);

        checkPostMemberId(post, userId);

        postRepository.delete(post);
    }

    // 업데이트, 삭제할 때 게시물 ID와 로그인한 유저 ID 일치하는지 확인
    private void checkPostMemberId(Post post, Long memberId){
        if(!post.getMemberId().equals(memberId)){
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_ACTION);
        }
    }
}
