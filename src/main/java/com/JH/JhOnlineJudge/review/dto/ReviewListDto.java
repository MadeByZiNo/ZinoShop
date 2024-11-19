package com.JH.JhOnlineJudge.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReviewListDto {

    private Long id;

    private String nickname;

    private LocalDateTime createdAt;

    private String content;

    private String imageUrl;

    @Builder
    private ReviewListDto(Long id, String nickname, LocalDateTime createdAt, String content, String imageUrl){
        this.id = id;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public static ReviewListDto of(Long id , String nickname, LocalDateTime createdAt, String content, String imageUrl){
        return ReviewListDto.builder()
                .id(id)
                .nickname(nickname)
                .createdAt(createdAt)
                .content(content)
                .imageUrl(imageUrl)
                .build();

    }
}
