package org.example.newsfeed.profile.repository;


import org.example.newsfeed.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface ProfileRepository extends JpaRepository<Member,Long> {


    default Member findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Does not exist id + "+ id));
    }


}