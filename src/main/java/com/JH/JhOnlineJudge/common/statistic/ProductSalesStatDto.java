package com.JH.JhOnlineJudge.common.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ProductSalesStatDto {
    private int quantity;
     private int price;
     private String date;
}
