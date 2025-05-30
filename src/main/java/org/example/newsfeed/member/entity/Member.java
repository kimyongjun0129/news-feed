package org.example.newsfeed.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.newsfeed.common.entity.BaseEntity;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;



    //멤버 사인업 리퀘스트 디티오 생성자
    public Member(String memberName, String email, String password) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
    }
}