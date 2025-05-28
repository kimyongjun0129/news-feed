package org.example.newsfeed.post.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.newsfeed.common.entity.BaseEntity;

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
    // 아마도 content 크기 늘어날 예정, DB와 length 값 수정해줄것
    @Column(length = 255)
    @NotNull
    private String content;

    private Long memberId;

    public Post() {}
    public Post(String title, String content, Long memberId){
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
    public void updateContent(String content) {
        this.content = content;
    }
}