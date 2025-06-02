CREATE TABLE members
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '식별자',
    member_name   VARCHAR(20) NOT NULL COMMENT '이름',
    email        VARCHAR(60) NOT NULL UNIQUE COMMENT '이메일',
    age           BIGINT COMMENT '나이',
    nickname      VARCHAR(20) NOT NULL COMMENT '별명',
    mbti         VARCHAR(4) NOT NULL COMMENT 'MBTI',
    intro        VARCHAR(20) NOT NULL COMMENT '자기소개',
    password     VARCHAR(255) NOT NULL COMMENT '비밀번호',
    is_deleted   BOOLEAN DEFAULT FALSE COMMENT '탈퇴 여부',
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일'
);

CREATE TABLE POSTS
(
    id          BIGINT  AUTO_INCREMENT PRIMARY KEY COMMENT '식별자',
    title       VARCHAR(20)     NOT NULL COMMENT '게시글 이름',
    content     TEXT            NOT NULL COMMENT '게시글 내용',
    member_id   BIGINT          NOT NULL COMMENT '유저 테이블',
    created_at  DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at  DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    FOREIGN KEY (member_id) REFERENCES members(id)
);

CREATE TABLE follows
(
     id          BIGINT  AUTO_INCREMENT PRIMARY KEY COMMENT '식별자',
     follower_id BIGINT  NOT NULL COMMENT '팔로워 아이디',
     followee_id BIGINT  NOT NULL COMMENT '팔로위 아이디',
     UNIQUE (follower_id, followee_id),
     FOREIGN KEY (follower_id) REFERENCES members(id),
     FOREIGN KEY (followee_id) REFERENCES members(id),
     CHECK ( follower_id <> followee_id )
);

CREATE TABLE comments
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '식별자',
    content     VARCHAR(255)    NOT NULL COMMENT '댓글 내용',
    member_id   BIGINT          NOT NULL COMMENT '유저 테이블',
    post_id     BIGINT          NOT NULL COMMENT '게시글 테이블',
    created_at  DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at  DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (post_id) REFERENCES posts(id)
);

CREATE TABLE posts_likes
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '식별자',
    member_id   BIGINT          NOT NULL COMMENT '유저 테이블',
    post_id     BIGINT          NOT NULL COMMENT '게시글 테이블',
    created_at  DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at  DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

CREATE TABLE comments_likes
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '식별자',
    member_id   BIGINT          NOT NULL COMMENT '유저 테이블',
    comment_id     BIGINT          NOT NULL COMMENT '댓글 테이블',
    created_at  DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at  DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE
);
