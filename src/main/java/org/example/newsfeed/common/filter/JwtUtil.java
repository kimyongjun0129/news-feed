package org.example.newsfeed.common.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims validateAndParse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(Long memberId) {
        Date now = new Date();
        Date expireyDate = new Date(now.getTime() + 60 * 60 * 1000);

        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("memberId", memberId)
                .setIssuedAt(now)
                .setExpiration(expireyDate)
                .signWith(key)
                .compact();
    }

    public Long getMemberId(String token) {
        Claims claims = validateAndParse(token);
        return claims.get("memberId", Long.class);
    }
}