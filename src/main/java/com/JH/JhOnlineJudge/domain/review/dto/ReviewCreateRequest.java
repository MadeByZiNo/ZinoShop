package com.JH.JhOnlineJudge.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateRequest {
    private String content;
    private Long productId;
}
