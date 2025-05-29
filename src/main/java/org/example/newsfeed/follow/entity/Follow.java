package org.example.newsfeed.follow.entity;

import jakarta.persistence.*;
import org.example.newsfeed.member.entity.Member;

@Entity
@Table(name = "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "followee_id", nullable = false)
    private Member followee;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private Member follower;

    public Follow( Member followee, Member follower) {
        this.followee = followee;
        this.follower = follower;
    }

    public Follow() {
    }
}
