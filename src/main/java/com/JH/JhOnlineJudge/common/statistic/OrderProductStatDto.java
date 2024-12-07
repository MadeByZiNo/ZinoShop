package com.JH.JhOnlineJudge.common.statistic;

import com.JH.JhOnlineJudge.common.OrderProduct.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class OrderProductStatDto implements Serializable {
    private Long productId;
    private int quantity;
    private int price;

      public static OrderProductStatDto from(OrderProduct orderProduct) {
          return new OrderProductStatDto(
                  orderProduct.getProduct().getId(),
                  orderProduct.getQuantity(),
                  orderProduct.getPrice()
          );
      }
}
