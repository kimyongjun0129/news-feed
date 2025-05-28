package org.example.newsfeed.comment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.newsfeed.common.entity.BaseEntity;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(length = 255)
    @NotNull
    private String content;

    @Setter
    private Long memberId;

    @Setter
    private Long postId;

    public Comment() {}
    public Comment(String content, Long memberId, Long postId) {
        this.content = content;
        this.memberId = memberId;
        this.postId = postId;
    }
}
