package org.example.newsfeed.common.exception;

import lombok.Getter;
import org.example.newsfeed.common.exception.error.CustomErrorCode;

@Getter
public class CustomException extends RuntimeException{
    private final CustomErrorCode errorCode;

    public CustomException(CustomErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
