package org.example.newsfeed.like.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.like.dto.LikeResponseDto;
import org.example.newsfeed.like.dto.PostOrCommentLikesResponseDto;
import org.example.newsfeed.like.entity.Like;
import org.example.newsfeed.like.repository.LikeRepository;
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
    //private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public LikeResponseDto like(Long memberId, Long postId){
        //멤버 api 추가 후 주석 제거
        //Member member = memberRepository.findByIdOrElseThrow(memberId);
        Post post = postRepository.findPostByIdOrElseThrow(postId);

        // 본인 게시물에 좋아요 하려할 때
        if(post.getMemberId().equals(memberId)){
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

    @Transactional
    public void unlike(Long memberId, Long postId){
        Like like = likeRepository.findByMemberIdAndPostIdOrElseThrow(memberId, postId);
        likeRepository.delete(like);
    }

    public PostOrCommentLikesResponseDto findByPostId(Long memberId, Long postId){

        Post post = postRepository.findPostByIdOrElseThrow(postId);

        List<Like> likes = likeRepository.findMemberIdByPostId(postId);
        List<Long> memberIds = likes.stream()
                .map(Like::getMemberId)
                .collect(Collectors.toList());
        Long countLikes = likeRepository.countByPostId(postId);
        boolean likedByMe = likeRepository.existsByMemberIdAndPostId(memberId, postId);

        return new PostOrCommentLikesResponseDto(memberIds, countLikes, likedByMe);

    }

    public List<LikeResponseDto> findByMemberId(Long memberId){
        return likeRepository.findByMemberId(memberId)
                .stream()
                .map(LikeResponseDto::toDto)
                .toList();
    }
//
//    /**
//     * 게시물의 좋아요 수 반환
//     * @param postId
//     * @return
//     */
//    public LikeCountResponseDto countByPostId(Long postId){
//        return new LikeCountResponseDto(postId, likeRepository.countByPostId(postId));
//    }


}
