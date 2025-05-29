package org.example.newsfeed.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@Component
public class JwtFilter implements Filter {
    private static final String[] WHITE_LIST = {"/api/signup", "/api/users/login", "/api/users/*/profile"};

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        if(!isWhiteList(requestURI)) {
            if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
            {
                throw new AuthenticationException("유효한 JWT 가지고 있지 않음");
            }
        }
        // 아래는 서명, 만료 검증부분, gradle 에 dependency 추가하면 주석 해제
        String token = authorizationHeader.replace("Bearer ", "");
        // Claims claims = jwtUtil.validateAndParse(token);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}