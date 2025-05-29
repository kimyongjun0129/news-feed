package org.example.newsfeed.common.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.newsfeed.common.constant.SessionConstant;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

public class LoginFilter implements Filter {

    private static final String[] WHITE_LIST = {"/api/auth/signup", "/api/auth/login", "/api/users/*/profile"};

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        if(!isWhiteList(requestURI)) {
            HttpSession session = httpServletRequest.getSession(false);

            if(session == null || session.getAttribute(SessionConstant.MEMBER) == null)
            {
                throw new AuthenticationException("로그인을 먼저 시도해주세요.");
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
