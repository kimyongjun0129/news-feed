package org.example.newsfeed.comment.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.comment.dto.CommentResponseDto;
import org.example.newsfeed.comment.entity.Comment;
import org.example.newsfeed.comment.repository.CommentRepository;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.like.repository.projection.CommentLikeCount;
import org.example.newsfeed.like.repository.CommentLikeRepository;
import org.example.newsfeed.member.entity.Member;
import org.example.newsfeed.member.repository.MemberRepository;
import org.example.newsfeed.post.entity.Post;
import org.example.newsfeed.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final EntityManager entityManager;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;

    @Override
    public CommentResponseDto createComment(Long postId, String content, Long memberId) {
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        Post post = postRepository.findById(postId).orElse(null);
        // 해당 postId를 가진 post 없으면 예외처리
        if (post == null) {
            throw new CustomException(CustomErrorCode.POST_NOT_FOUND);
        }

        Comment comment = new Comment(content, member, post);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment);
    }

    @Override
    public Page<CommentResponseDto> findComments(Long postId, int page, int size) {
        // 해당 postId를 가진 post 없으면 예외처리
        if (postRepository.findById(postId).orElse(null) == null) {
            throw new CustomException(CustomErrorCode.POST_NOT_FOUND);
        }

        // 최신 순으로 정렬되어 있고, 다른 정렬 기준(좋아요순)이 필요하면 RequestParam 으로 받을 수 있을 것 같다
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Comment> commentPage = commentRepository.findAllByPostId(pageable, postId);

        // 좋아요 수 받아올 댓글 ID List
        List<Long> commentIdList = commentPage.getContent().stream().map(Comment::getId).toList();
        List<CommentLikeCount> result = commentLikeRepository.countLikesByCommentIds(commentIdList);
        Map<Long, Long> likesCountMap = new HashMap<>();
        for (CommentLikeCount row : result) {
            Long commentId = row.getCommentId();
            Long likeCount = row.getCount();
            likesCountMap.put(commentId, likeCount);
        }

        return commentPage.map(comment -> {
                    CommentResponseDto responseDto = new CommentResponseDto(comment);
                    responseDto.setLikeCount(likesCountMap.getOrDefault(comment.getId(), 0L));
                    return responseDto;
                }
        );
    }

    @Transactional
    @Override
    public CommentResponseDto updateComment(Long postId, Long commentId, String content, Long memberId) {
        Comment comment = checkPostAndMember(postId, commentId, memberId);

        // 수정 내용 없을 때
        if (content == null) {
            throw new CustomException(CustomErrorCode.INVALID_COMMENT_UPDATE);
        }

        // 기존 댓글과 동일할 때
        if (content.equals(comment.getContent())) {
            throw new CustomException(CustomErrorCode.INVALID_COMMENT_UPDATE);
        }

        comment.setContent(content);

        // 영속성 context db 로 전달 -> updatedAt 시간 최신화
        // @Transactional 때문에 모든 작업 끝나야 update 되기 때문에 flush()없으면 이전에 가지고 있던 updatedAt 리턴
        entityManager.flush();
        return new CommentResponseDto(comment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId, Long memberId) {
        Comment comment = checkPostAndMember(postId, commentId, memberId);

        commentRepository.delete(comment);
    }

    // PostId, MemberId 검증
    private Comment checkPostAndMember(Long postId, Long commentId, Long memberId) {
        // 해당 postId를 가진 post 없으면 예외처리
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null) {
            throw new CustomException(CustomErrorCode.POST_NOT_FOUND);
        }

        // 로그인한 memberId 와 작성자 memberId 가 다를 경우 예외처리
        Comment comment = commentRepository.findCommentByIdOrElseThrow(commentId);
        if(!memberId.equals(comment.getMember().getId())){
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_ACTION);
        }

        return comment;
    }
}
