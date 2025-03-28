package com.JH.JhOnlineJudge.domain.user.service;

import com.JH.JhOnlineJudge.common.utils.JwtUtil;
import com.JH.JhOnlineJudge.common.utils.ObjectSerializer;
import com.JH.JhOnlineJudge.domain.user.entity.User;
import com.JH.JhOnlineJudge.domain.user.dto.UserTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor

public class TokenService {

    @Value("${jwt.accessExpire}")
    private long ACCESS_EXPIRE;

    @Value("${jwt.refreshExpire}")
    private long REFRESH_EXPIRE;

    private final ObjectSerializer objectSerializer;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    public void saveAuthInfo(String accessToken, UserTokenDto userTokenDto) {
        objectSerializer.saveData("auth:" + accessToken, userTokenDto, Duration.ofMillis(ACCESS_EXPIRE));
    }

    // RefreshToken 저장
    public void saveRefreshToken(String refreshToken, Long userId) {
        objectSerializer.saveData("refresh:" + userId, refreshToken, Duration.ofMillis(REFRESH_EXPIRE));
    }

    // RefreshToken 조회
    public String getRefreshToken(Long userId) {
        return objectSerializer.getData("refresh:" + userId, String.class)
                .orElse(null);
    }

    // AccessToken에 저장된 authInfo 조회
    public UserTokenDto getAuthInfo(String accessToken) {
        Optional<UserTokenDto> userTokenDtoOptional = objectSerializer.getData("auth:" + accessToken, UserTokenDto.class);

        return userTokenDtoOptional.orElse(null);
    }

    // 토큰 만료 시 삭제
    public void deleteAuthInfo(String accessToken) {
        redisTemplate.delete("auth:" + accessToken);
    }

    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete("refresh:" + userId);
    }

    // 토근 발급
    public ConcurrentHashMap<String, String> issueJwt(User user) {
        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getNickname(), user.getRole(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getNickname(), user.getRole(), user.getId());

        // redis에 저장
        UserTokenDto userTokenDto = UserTokenDto.of(user.getId(), user.getUsername(), user.getNickname(), user.getRole());
        saveAuthInfo(accessToken, userTokenDto);
        saveRefreshToken(refreshToken, user.getId());

        ConcurrentHashMap<String,String> tokenMap = new ConcurrentHashMap();
        tokenMap.put("accessToken",accessToken);
        tokenMap.put("refreshToken",refreshToken);

        return tokenMap;
    }
}


