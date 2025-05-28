package org.example.newsfeed.post.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.entity.Post;
import org.example.newsfeed.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public PostResponseDto createPost(String title, String content) {

        Post post = new Post(title, content, 1L);

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost);
    }

    @Override
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findPostByIdOrElseThrow(id);
        return new PostResponseDto(post);
    }

    @Override
    public Page<PostResponseDto> findPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Post> postPage = postRepository.findAll(pageable);

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

    // 적합한 exception 으로 변경하기
    private void checkPostMemberId(Post post, Long memberId){
        if(!post.getMemberId().equals(memberId)){
            throw new RuntimeException(
                    "The post doesn't have a member of this post"
            );
        }
    }
}
