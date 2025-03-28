package com.JH.JhOnlineJudge.domain.user.dto;

import com.JH.JhOnlineJudge.domain.user.entity.UserRole;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenDto {
    private Long userId;
    private String username;
    private String nickname;
    private UserRole role;

    public static UserTokenDto of(Long userId, String username, String nickname, UserRole role) {
        return UserTokenDto.builder()
                .userId(userId)
                .username(username)
                .nickname(nickname)
                .role(role)
                .build();
    }
}
