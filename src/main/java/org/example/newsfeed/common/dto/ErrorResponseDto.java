package org.example.newsfeed.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponseDto {

    private final int status;
    private final String message;

}

