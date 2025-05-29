package org.example.newsfeed.like.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.newsfeed.common.entity.BaseEntity;

@Getter
@Entity
@Table(name = "posts_likes")
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long memberId;

    @Column
    private Long postId;

    public Like(){}
    public Like(Long memberId, Long postId){
        this.memberId = memberId;
        this.postId = postId;
    }
}

