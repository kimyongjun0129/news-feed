package org.example.newsfeed.comment.repository.projection;

public interface CommentCount {
    Long getPostId();
    Long getCount();
}
