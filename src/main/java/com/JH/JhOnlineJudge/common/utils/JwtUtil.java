package com.JH.JhOnlineJudge.common.utils;

import com.JH.JhOnlineJudge.domain.user.entity.UserRole;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    public String generateAccessToken(String username, String nickname, UserRole userRole, Long userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("nickname",nickname)
                .claim("role",userRole)
                .claim("userId",userId)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String username, String nickname, UserRole userRole, Long userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("nickname",nickname)
                .claim("role",userRole)
                .claim("userId",userId)
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

    // 토큰에서 역할 추출
    public Long getUserId(String token) {
        Integer userId = (Integer) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("userId");
        return userId != null ? userId.longValue() : null;
    }


    public static void addJwtToken(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setPath("/");
        accessCookie.setHttpOnly(true);
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        response.addCookie(refreshCookie);
    }
}