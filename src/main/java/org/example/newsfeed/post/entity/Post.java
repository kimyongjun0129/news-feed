package org.example.newsfeed.post.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.newsfeed.comment.entity.Comment;
import org.example.newsfeed.common.entity.BaseEntity;
import org.example.newsfeed.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "posts")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(length = 20)
    @NotNull
    private String title;

    @Setter
    @Column(columnDefinition = "TEXT")
    @NotNull
    private String content;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // post : comment = 1: N 매핑 관계, post 삭제 시 동시 삭제, post 없는 comments 삭제
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    public Post() {}
    public Post(String title, String content, Member member){
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public int updateTitle(String title) {
        if(this.title.equals(title)) return 0;
        this.title = title;
        return 1;
    }
    public int updateContent(String content) {
        if(this.content.equals(content)) return 0;
        this.content = content;
        return 1;
    }
}