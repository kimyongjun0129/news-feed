package org.example.newsfeed.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.newsfeed.member.dto.LoginRequestDto;
import org.example.newsfeed.member.dto.LoginResponseDto;
import org.example.newsfeed.member.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/")
public class AuthController {

    private final AuthService authorService;

    public AuthController(AuthService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletRequest httpServletRequest
    ) {
        LoginResponseDto loginResponseDto = authorService.login(loginRequestDto, httpServletRequest);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }
}
