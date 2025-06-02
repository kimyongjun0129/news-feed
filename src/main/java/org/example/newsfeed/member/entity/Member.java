package org.example.newsfeed.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.newsfeed.comment.entity.Comment;
import org.example.newsfeed.common.entity.BaseEntity;
import org.example.newsfeed.post.entity.Post;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    private boolean isDeleted = false;

    @Column(nullable = false)
    private int age;

    @Setter
    @Column(length = 20, nullable = false)
    private String nickname;

    @Setter
    @Column(nullable = false,length = 20)
    private String intro;

//    //엠비티아이
    @Setter
    @Column(length = 4)
    private String mbti;

    @OneToMany(mappedBy = "member")
    private final List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private final List<Comment> comments = new ArrayList<>();

    //멤버 사인업 리퀘스트 디티오 생성자
    public Member(String memberName, String email, String password, int age, String nickname, String intro, String mbti) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.age =age;
        this.nickname = nickname;
        this.intro=intro;
        this.mbti=mbti;
    }

    public Member(String memberName, String email, String password) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
    }

    public void delete() {
        this.isDeleted = true;
    }
}