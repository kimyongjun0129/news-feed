package org.example.newsfeed.comment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.newsfeed.common.entity.BaseEntity;
import org.example.newsfeed.member.entity.Member;
import org.example.newsfeed.post.entity.Post;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotNull
    private String content;

    // comment : member = N : 1 관계, 지연로딩, member 필수
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // comment : member = N : 1 관계, 지연로딩, member 필수
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Comment() {}
    public Comment(String content, Member member, Post post) {
        this.content = content;
        this.member = member;
        this.post = post;
    }
}