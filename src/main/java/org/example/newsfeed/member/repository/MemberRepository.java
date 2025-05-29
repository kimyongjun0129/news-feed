package org.example.newsfeed.member.repository;

import org.example.newsfeed.common.exception.CustomException;
import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.member.entity.Member;
import org.example.newsfeed.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);

    // Member 반환, 없다면 USER_NOT_FOUND 예외 발생
    default Member findMemberByIdOrElseThrow(Long id){
        return findById(id)
                .orElseThrow(() ->
                        new CustomException(CustomErrorCode.USER_NOT_FOUND)
                );
    }

    Member getMemberByEmail(String email);
}

