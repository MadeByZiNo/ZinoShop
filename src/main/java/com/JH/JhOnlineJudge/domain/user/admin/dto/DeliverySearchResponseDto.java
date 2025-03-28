package com.JH.JhOnlineJudge.domain.user.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class DeliverySearchResponseDto {
    private Long orderId;
    private String name;
    private String username;
    private int price;
    private String status;
    private String recipientName;
    private String recipientAddress;
    private String memo;
    private LocalDateTime orderAt;
}
