package org.example.newsfeed.common.config;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.constant.UserRole;
import org.example.newsfeed.common.filter.JwtFilter;
import org.example.newsfeed.common.filter.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 인증
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/logout").hasRole(UserRole.ADMIN.name())
                        .requestMatchers("/api/auth/signup").permitAll()
                        .requestMatchers("/api/auth/delete").hasRole(UserRole.ADMIN.name())

                        // 게시물 (post)
                        .requestMatchers(HttpMethod.POST, "/api/posts").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/posts").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/posts/*").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/posts/*").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/*").hasRole(UserRole.ADMIN.name())

                        // 댓글 (comment)
                        .requestMatchers(HttpMethod.POST, "/api/posts/*/comments").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/posts/*/comments").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/posts/*/comments/*").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/*/comments/*").hasRole(UserRole.ADMIN.name())

                        // 프로필 (profile)
                        .requestMatchers(HttpMethod.PATCH, "/api/users/*/profile").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/users/*/profile").permitAll()

                        // 게시물 좋아요 (PostLike)
                        .requestMatchers(HttpMethod.POST, "/api/posts/*/likes").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/*/likes").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/posts/*/likes").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/members/*/likes").hasRole(UserRole.ADMIN.name())

                        // 댓글 좋아요 (CommentLike)
                        .requestMatchers(HttpMethod.POST, "/api/posts/*/comments/*/likes").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/*/comments/*/likes").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/posts/*/comments/*/likes").hasRole(UserRole.ADMIN.name())

                        // 댓글 좋아요 (PostLikeController)
                        .requestMatchers(HttpMethod.GET, "/api/posts/*/likes").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/members/*/likes").hasRole(UserRole.ADMIN.name())

                        // 팔로우 기능 (follow)
                        .requestMatchers(HttpMethod.POST, "/api/members/*/follow").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/members/*/unfollow").hasRole(UserRole.ADMIN.name())
                )
                // 필터 등록
                .addFilterBefore(new JwtFilter(redisTemplate, jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(configurer ->
                        configurer
                                .authenticationEntryPoint(customAuthenticationEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler)
                )
                .build();
    }
}
