package org.example.newsfeed.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.newsfeed.common.entity.BaseEntity;

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


    @Column(nullable = false)
    private int age;

    @Setter
    @Column(length = 20, nullable = false, unique = false)
    private String nickname;

    @Setter
    @Column(nullable = false,length = 20)
    private String intro;

//    //엠비티아이
    @Setter
    @Column(length = 4)
    private String mbti;


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
}