package org.example.newsfeed.like.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.newsfeed.common.entity.BaseEntity;

@Getter
@Entity
@Table(name = "comments_likes")
public class CommentLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long memberId;

    @Column
    private Long commentId;

    public CommentLike(){}
    public CommentLike(Long memberId, Long commentId){
        this.memberId = memberId;
        this.commentId = commentId;
    }
}

