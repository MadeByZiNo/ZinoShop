package com.JH.JhOnlineJudge.user.admin.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ProductSalesRankDto {
     private String name;
     private Integer quantity;
     private Integer price;

     public void addQuantity(Integer quantity){this.quantity += quantity;}
     public void addPrice(Integer price){this.price += price;}

}
