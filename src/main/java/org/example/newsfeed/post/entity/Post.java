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
    @Column(columnDefinition = "TEXT")
    @NotNull
    private String content;

    private Long memberId;

    public Post() {}
    public Post(String title, String content, Long memberId){
        this.title = title;
        this.content = content;
        this.memberId = memberId;
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