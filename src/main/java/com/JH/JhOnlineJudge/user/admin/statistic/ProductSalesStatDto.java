package com.JH.JhOnlineJudge.user.admin.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Setter
@Getter
public class ProductSalesStatDto {
     private Integer quantity;
     private Integer price;
     private String date;
}
