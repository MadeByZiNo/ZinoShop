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

    public String generateAccessToken(String username, String nickname, UserRole userRole) {
        return Jwts.builder()
                .setSubject(username)
                .claim("nickname",nickname)
                .claim("role",userRole)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String username, String nickname, UserRole userRole) {
        return Jwts.builder()
                .setSubject(username)
                .claim("nickname",nickname)
                .claim("role",userRole)
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

    // 토큰에서 역할 추출
     public UserRole getRole(String token) {
         return  UserRole.valueOf((String) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("role"));
     }
}