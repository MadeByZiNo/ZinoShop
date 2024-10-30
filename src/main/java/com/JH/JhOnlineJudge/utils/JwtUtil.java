package com.JH.JhOnlineJudge.utils;

import com.JH.JhOnlineJudge.user.domain.UserRole;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Value("${jwt.accessExpire}")
    private long ACCESS_EXPIRE;

    @Value("${jwt.refreshExpire}")
    private long REFRESH_EXPIRE;

    private final RedisTemplate<String, String> redisTemplate;

    public String generateAccessToken(String username, String nickname) {
        return Jwts.builder()
                .setSubject(username)
                .claim("nickname",nickname)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String username, String nickname) {
        return Jwts.builder()
                .setSubject(username)
                .claim("nickname",nickname)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean isInvalidToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return false;
        } catch (JwtException e) {
            return true;
        }
    }

    // 토큰에서 아이디 추출
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰에서 닉네임 추출
    public String getNickname(String token) {
        return (String) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("nickname");
    }

}