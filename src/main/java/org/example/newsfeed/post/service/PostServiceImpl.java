package org.example.newsfeed.post.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.comment.repository.CommentRepository;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.like.repository.LikeRepository;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.entity.Post;
import org.example.newsfeed.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final EntityManager entityManager;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

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

        // page content 담당 post 들의 postId 리스트 생성
        List<Long> postIdList = postPage.getContent().stream()
                .map(Post::getId)
                .toList();

        // postId 별 좋아요 수 매핑
        List<Object[]> likesResult = likeRepository.countLikesByPostIds(postIdList);
        Map<Long, Long> likesCountMap = new HashMap<>();
        for(Object[] row : likesResult) {
            Long postId = (Long) row[0];
            Long likeCount = (Long) row[1];
            likesCountMap.put(postId, likeCount);
        }
        // postId 별 댓글 수 매핑
        List<Object[]> commentsResult = commentRepository.countCommentsByPostIds(postIdList);
        Map<Long, Long> commentCountMap = new HashMap<>();
        for(Object[] row : commentsResult) {
            Long postId = (Long) row[0];
            Long commentCount = (Long) row[1];
            commentCountMap.put(postId, commentCount);
        }

        // postId에 좋아요 수, 댓글 수 포함시켜서 response 반환
        return postPage.map(post -> {
            PostResponseDto responseDto = new PostResponseDto(post);
            responseDto.setLikeCount(likesCountMap.getOrDefault(post.getId(), 0L));
            responseDto.setCommentCount(commentCountMap.getOrDefault(post.getId(), 0L));
            return responseDto;
        });
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long id, String title, String content, Long userId) {
        Post post = postRepository.findPostByIdOrElseThrow(id);

        checkPostMemberId(post, userId);

        // 적합한 에러 코드 필요(아무 내용 없이 업데이트 불가)
        if(title == null && content == null){
            throw new CustomException(CustomErrorCode.INVALID_POST_UPDATE, "제목, 내용 중 최소 하나 이상의 입력이 필요합니다.");
        }

        int update = 0;
        if(title != null){
            update += post.updateTitle(title);
        }
        if(content != null){
            update += post.updateContent(content);
        }

        // 적합한 에러 코드 필요(변경사항 없이 업데이트 불가)
        if(update == 0){
            throw new CustomException(CustomErrorCode.INVALID_POST_UPDATE, "변경사항 없이 업데이트 불가능합니다.");
        }

        entityManager.flush();
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
