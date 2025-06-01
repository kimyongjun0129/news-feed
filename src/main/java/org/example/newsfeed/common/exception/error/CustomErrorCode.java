package org.example.newsfeed.common.exception.error;

import lombok.Getter;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomErrorCode {
    EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "이메일 형식이 잘못되었습니다."), // (회원가입, 로그인) 이메일 형식 틀림
    PASSWORD_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "비밀번호 형식이 잘못되었습니다."), // (회원가입, 로그인) 비번 형식 틀림
    USERNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "유저네임 형식이 잘못되었습니다."), // (회원가입, 로그인) 유저네임 형식 틀림
    MBTI_INVALID_FORMAT(HttpStatus.BAD_REQUEST,"MBTI는 정확히 4글자여야 합니다."), //(회원가입, 프로필 수정) MBTI 형식 틀림
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."), // (로그인) 저장된 이메일 없음
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 잘못되었습니다."), // (로그인, 비번 변경) 비밀번호 틀림
    NEW_PASSWORD_SAME_AS_OLD(HttpStatus.CONFLICT, "현재 비밀번호와 다른 비밀번호를 사용해야합니다."), // (비번 변경) 전 비밀번호랑 같은 비밀번호 입력
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."), // (로그인) 로그인 필요 서비스에 로그인 안하고 접근

    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."), // (회원가입) 이메일 중복
    DELETED_ACCOUNT(HttpStatus.GONE, "탈퇴한 사용자 이메일입니다."), // (회원가입) 이미 탈퇴한 이메일

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."), // (친구추가) 사용자 없음
    FOLLOW_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 팔로우 한 사용자입니다."), // (친구추가) 팔로우 한 유저를 다시 팔로우할때
    SELF_FOLLOW_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "본인을 팔로우 할 수 없습니다."), // (친구추가) 본인 팔로우

    UNAUTHORIZED_ACTION(HttpStatus.FORBIDDEN, "작성자가 아닙니다."), // (게시글, 댓글 수정 및 삭제) 게시물의 작성자, 댓글의 작성자, 댓글의 원 게시물 작성자 아닐 때
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."), // (게시글 조회) 저장된 게시물 없음

    INVALID_SORT_OPTION(HttpStatus.BAD_REQUEST, "정렬 조건이 잘못되었습니다."), // (게시글 정렬) 잘못된 정렬 조건 입력
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "날짜 형식이 잘못되었습니다."), // (게시글 검색) 날짜 형식 틀림

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."), // (댓글 조회) 저장된 댓글 없음
    INVALID_COMMENT_UPDATE(HttpStatus.BAD_REQUEST, "댓글 내용만 수정할 수 있습니다."), // (댓글 수정) 내용 외의 필드를 수정하려고 할 때

    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 찾을 수 없습니다."), // (좋아요) 저장된 좋아요 없음
    LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 좋아요 했습니다."), // (좋아요 추가) 이미 좋아요 한 게시물, 댓글
    CANNOT_LIKE_OWN_POST(HttpStatus.BAD_REQUEST, "본인이 작성한 게시물을 좋아요 할 수 없습니다."), // (좋아요 추가) 본인이 작성한 게시물에 좋아요
    CANNOT_LIKE_OWN_COMMENT(HttpStatus.BAD_REQUEST, "본인이 작성한 댓글을 좋아요 할 수 없습니다."); // (좋아요 추가) 본인이 작성한 댓글에 좋아요


    private final HttpStatus httpStatus;
    private final String message;

    CustomErrorCode(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }
}