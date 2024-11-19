package com.JH.JhOnlineJudge.review.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ReviewCreateDto {
    private String content;
    private Long productId;
}
