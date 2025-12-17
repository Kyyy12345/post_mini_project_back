package com.korit.post_mini_project_back.jwt;

import com.korit.post_mini_project_back.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

//IoC에 등록 Component
@Component
public class JwtTokenProvider {

    private final SecretKey key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAuthenticationToken(User userEntity) {
        Date now = new Date();
        Long expiredTime = now.getTime() + (1000l * 60l * 60l * 24l);
        Date expiredDate = new Date(expiredTime);

        return Jwts.builder()
                .subject("Server Access Token")
                .issuer("KYY")
                .issuedAt(new Date()) // 밑에 expiration, claim은 필수
                .expiration(expiredDate) // 필수
                .claim("userId", userEntity.getUserId()) // 필수
                .signWith(key, SignatureAlgorithm.HS256) // 필수
                .compact();
    }

    public boolean validateToken(String token) {
        Claims claims = null;

        try {
            // parser가 토큰 -> claim으로 변환
            JwtParser jwtParser = Jwts.parser()
                    .setSigningKey(key)
                    .build();

            jwtParser.parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            // parse하다가 문제있어서 예외 터지면 ex. 시간이 지나거나
            return false;
        }

    }

    public int getUserId(String token) {
        return (int) Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .get("userId");

    }
}
