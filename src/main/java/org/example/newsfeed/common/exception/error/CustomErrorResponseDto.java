package org.example.newsfeed.common.exception.error;

public class CustomErrorResponseDto {
    private String code;
    private String message;

    public CustomErrorResponseDto(String code, String message){
        this.code = code;
        this.message = message;
    }
}
