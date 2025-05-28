package org.example.newsfeed.comment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import org.example.newsfeed.common.entity.BaseEntity;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
    private Long id;
    private String content;
    private Long memberId;
    private Long postId;
}
