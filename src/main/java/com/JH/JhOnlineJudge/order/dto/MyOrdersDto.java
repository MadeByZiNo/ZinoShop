package com.JH.JhOnlineJudge.order.dto;

import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyOrdersDto {
    private String externalId;
    private LocalDateTime orderAt;
    private String name;
    private OrderStatus status;
    private String recipientName;
    private String recipientAddress;
    private int totalPrice;

    @Builder
    public MyOrdersDto(String externalId, LocalDateTime orderAt, String name, OrderStatus status, String recipientName, String recipientAddress, int totalPrice) {
        this.externalId = externalId;
        this.orderAt = orderAt;
        this.name = name;
        this.status = status;
        this.recipientName = recipientName;
        this.recipientAddress = recipientAddress;
        this.totalPrice = totalPrice;
    }

    public static MyOrdersDto of(String externalId, LocalDateTime orderAt, String name,
                                 OrderStatus status, String recipientName, String recipientAddress, int totalPrice) {
          return MyOrdersDto.builder()
                            .externalId(externalId)
                            .orderAt(orderAt)
                            .name(name)
                            .status(status)
                            .recipientName(recipientName)
                            .recipientAddress(recipientAddress)
                            .totalPrice(totalPrice)
                            .build();
      }
}
