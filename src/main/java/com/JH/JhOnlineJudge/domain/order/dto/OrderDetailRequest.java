package com.JH.JhOnlineJudge.domain.order.dto;

import com.JH.JhOnlineJudge.domain.order.entity.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderDetailRequest {
    
    private int totalPrice;
    private int discountedPrice;
    private int finalPrice;
    private String recipientName;
    private String recipientAddress;
    private String paymentMethod;
    private String memo;
    private String discountInfo;
    private List<OrderProductDetailDto> products;

    @Builder
     private OrderDetailRequest(int totalPrice, int discountedPrice, int finalPrice, String recipientName,
                                String recipientAddress, String paymentMethod, String memo, String discountInfo,
                                List<OrderProductDetailDto> products) {
        this.totalPrice = totalPrice;
        this.discountedPrice = discountedPrice;
        this.finalPrice = finalPrice;
        this.recipientName = recipientName;
        this.recipientAddress = recipientAddress;
        this.paymentMethod = paymentMethod;
        this.memo = memo;
        this.discountInfo = discountInfo;
        this.products = products;
    }

    public static OrderDetailRequest of(Order order, List<OrderProductDetailDto> products) {
        return OrderDetailRequest.builder().
            totalPrice(order.getTotalPrice()).
            discountedPrice(order.getDiscountedPrice()).
            finalPrice(order.getFinalPrice()).
            recipientName(order.getRecipientName()).
            recipientAddress(order.getRecipientAddress()).
            paymentMethod(order.getPaymentMethod()).
            memo(order.getMemo()).
            discountInfo(order.getDiscountInfo()).
            products(products).
                build();
    }
}
