package org.example.newsfeed.post.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.comment.repository.CommentRepository;
import org.example.newsfeed.comment.repository.projection.CommentCount;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.like.repository.LikeRepository;
import org.example.newsfeed.like.repository.projection.PostLikeCount;
import org.example.newsfeed.member.entity.Member;
import org.example.newsfeed.member.repository.MemberRepository;
import org.example.newsfeed.post.dto.PostResponseDto;
import org.example.newsfeed.post.entity.Post;
import org.example.newsfeed.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final MemberRepository memberRepository;

    @Override
    public PostResponseDto createPost(String title, String content, Long memberId) {
        Member member = memberRepository.findByIdOrElseThrow(memberId);

        Post post = new Post(title, content, member);

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost);
    }

    @Override
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findPostByIdOrElseThrow(id);

        Long likes = likeRepository.countByPostId(id);
        Long comments = commentRepository.countByPostId(id);

        PostResponseDto responseDto = new PostResponseDto(post);
        responseDto.setLikeCount(likes);
        responseDto.setCommentCount(comments);

        return responseDto;
    }

    @Override
    public Page<PostResponseDto> findPosts(LocalDateTime from, LocalDateTime to, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Post> postPage = postRepository.findAllByUpdatedAtBetween(from, to, pageable);

        // page content 담당 post 들의 postId 리스트 생성
        List<Long> postIdList = postPage.getContent().stream()
                .map(Post::getId)
                .toList();

        // 조회할 각 게시물의 좋아요 수 가져오기
        List<PostLikeCount> likesResult = likeRepository.countLikesByPostIds(postIdList);
        Map<Long, Long> likesCountMap = new HashMap<>();
        for(PostLikeCount row : likesResult) {
            Long postId = row.getPostId();
            Long likeCount = row.getCount();
            likesCountMap.put(postId, likeCount);
        }
        // 조회할 각 게시물의 댓글 수 가져오기
        List<CommentCount> commentsResult = commentRepository.countCommentsByPostIds(postIdList);
        Map<Long, Long> commentCountMap = new HashMap<>();
        for(CommentCount row : commentsResult) {
            Long postId = row.getPostId();
            Long commentCount = row.getCount();
            commentCountMap.put(postId, commentCount);
        }

        // Post 로 생성한 responseDto 에  좋아요, 댓글 수 포함시켜서 반환
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

        // 제목, 내용 둘다 없을 때 예외처리
        if(title == null && content == null){
            throw new CustomException(CustomErrorCode.INVALID_POST_UPDATE, "제목, 내용 중 최소 하나 이상의 입력이 필요합니다.");
        }

        // 변경사항 있으면 +1 하고 변경사항 반영, 없으면 0만 반환
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

        // 영속성 context db 로 전달 -> updatedAt 시간 최신화
        // @Transactional 때문에 모든 작업 끝나야 update 되기 때문에 flush()없으면 이전에 가지고 있던 updatedAt 리턴
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
        if(!post.getMember().getId().equals(memberId)){
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_ACTION);
        }
    }
}
