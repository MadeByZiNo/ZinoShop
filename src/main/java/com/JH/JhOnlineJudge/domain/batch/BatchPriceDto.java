package com.JH.JhOnlineJudge.domain.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BatchPriceDto {
    private Long userId;
    private Long  totalPrice;
}
