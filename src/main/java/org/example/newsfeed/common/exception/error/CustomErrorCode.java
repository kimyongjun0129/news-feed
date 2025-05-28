package org.example.newsfeed.common.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomErrorCode {
    EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "이메일 형식이 잘못되었습니다."), // 회원가입, 로그인 이메일 형식 틀림
    PASSWORD_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "비밀번호 형식이 잘못되었습니다."), //회원가입, 로그인 비번 형식 틀림
    USERNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "유저네임 형식이 잘못되었습니다."), // 회원가입, 로그인 유저네임 형식 틀림
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."), // 로그인 저장된 이메일 없음
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 잘못되었습니다."), // 비밀번호 틀림
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."), // 회원가입 이메일 중복
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."), // 친구추가 ID 없음
    DELETED_ACCOUNT(HttpStatus.GONE, "탈퇴한 사용자 이메일입니다."), // 이미 탈퇴한 이메일
    UNAUTHORIZED_ACTION(HttpStatus.FORBIDDEN, "게시글 작성자가 아닙니다."); // 작성자 아닌데 게시글 수정, 삭제

    private final HttpStatus httpStatus;
    private final String message;

    CustomErrorCode(HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.message = message;
    }
}