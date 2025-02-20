package com.JH.JhOnlineJudge.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExtractPaymentDto {
        private String externalId;
        private String paymentKey;
        private String method;
}
