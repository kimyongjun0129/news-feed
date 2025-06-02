package org.example.newsfeed.like.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.like.dto.LikeResponseDto;
import org.example.newsfeed.like.dto.PostOrCommentLikesResponseDto;
import org.example.newsfeed.like.entity.Like;
import org.example.newsfeed.like.repository.LikeRepository;
import org.example.newsfeed.member.entity.Member;
import org.example.newsfeed.member.repository.MemberRepository;
import org.example.newsfeed.post.entity.Post;
import org.example.newsfeed.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /**
     * 좋아요 생성
     */
    public LikeResponseDto like(Long memberId, Long postId){

        Post post = postRepository.findPostByIdOrElseThrow(postId);

        // 본인 게시물에 좋아요 하려할 때
        if(post.getMember().getId().equals(memberId)){
            throw new CustomException(CustomErrorCode.CANNOT_LIKE_OWN_POST);
        }

        // 이미 좋아요 한 게시물에 좋아요 하려할 때
        if(likeRepository.findByMemberIdAndPostId(memberId, postId).isPresent()){
            throw new CustomException(CustomErrorCode.LIKE_ALREADY_EXISTS);
        }

        Like like = new Like(memberId, postId);
        likeRepository.save(like);

        return new LikeResponseDto(like.getId(), like.getMemberId(), like.getPostId());
    }

    /**
     * 좋아요 취소
     */
    @Transactional
    public void unlike(Long memberId, Long postId){
        // 게시물이 존재하지 않을 때
        Post post = postRepository.findPostByIdOrElseThrow(postId);

        Like like = likeRepository.findByMemberIdAndPostIdOrElseThrow(memberId, postId);
        likeRepository.delete(like);
    }

    /**
     * 게시물 id로 좋아요 조회
     */
    public PostOrCommentLikesResponseDto findByPostId(Long memberId, Long postId){

        // 게시물이 존재하지 않을 때
        Post post = postRepository.findPostByIdOrElseThrow(postId);

        List<Like> likes = likeRepository.findMemberIdByPostId(postId);
        List<Long> memberIds = likes.stream()
                .map(Like::getMemberId)
                .collect(Collectors.toList());
        Long countLikes = likeRepository.countByPostId(postId);
        boolean likedByMe = likeRepository.existsByMemberIdAndPostId(memberId, postId);

        return new PostOrCommentLikesResponseDto(memberIds, countLikes, likedByMe);

    }

    /**
     * member id로 좋아요 조회
     */
    public List<LikeResponseDto> findByMemberId(Long memberId){
        Member member = memberRepository.findByIdOrElseThrow(memberId);

        return likeRepository.findByMemberId(memberId)
                .stream()
                .map(LikeResponseDto::toDto)
                .toList();
    }
}
