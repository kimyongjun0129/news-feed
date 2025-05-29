package org.example.newsfeed.common.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.constant.SessionConstant;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {
    private final JwtUtil jwtUtil;

    private static final String[] WHITE_LIST = {"/api/auth/signup", "/api/auth/login", "/api/auth/*/profile"};

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String authorizationHeader = httpServletRequest.getHeader(SessionConstant.TOKEN);

        // 화이트리스트 검증
        if (!isWhiteList(requestURI)) {
            // 토큰 유효성 확인
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new ServletException("유효한 JWT 가지고 있지 않음");
            }

            // 토큰 서명 유효성, 만료 여부 확인
            String token = authorizationHeader.substring(7);
            Claims claims = null;
            try {
                claims = jwtUtil.validateAndParse(token);
            } catch (JwtException e) {
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.getWriter().write("Invalid JWT: " + e.getMessage());
            }

            // request 에 memberId 삽입
            if (claims != null) {
                Long memberId = jwtUtil.getMemberId(claims);
                httpServletRequest.setAttribute("memberId", memberId);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}