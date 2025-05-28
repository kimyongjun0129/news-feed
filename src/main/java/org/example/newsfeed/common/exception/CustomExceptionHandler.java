package org.example.newsfeed.common.exception;

import org.example.newsfeed.common.exception.error.CustomErrorCode;
import org.example.newsfeed.common.exception.error.CustomErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponseDto> handleCustomException(CustomException e){
        CustomErrorCode errorCode = e.getErrorCode();

        CustomErrorResponseDto errorResponseDto = new CustomErrorResponseDto(errorCode.name(),errorCode.getMessage());

        return new ResponseEntity<>(errorResponseDto, errorCode.getHttpStatus());
    }
}
