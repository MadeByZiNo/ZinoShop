package com.JH.JhOnlineJudge.order.domain;

import com.JH.JhOnlineJudge.common.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.common.OrderProduct.OrderProduct;
import com.JH.JhOnlineJudge.order.dto.OrderSaveDto;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime order_at;

    @Column(nullable = true)
    private LocalDateTime delivered_at;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = true)
    private String paymentKey;

    @Column(nullable = false)
    private int total_price;

    @Column(nullable = false)
    private int discounted_price;

    @Column(nullable = false)
    private int final_price;

    @Column(nullable = false, length = 50)
    private String recipient_name;

    @Column(nullable = false, length = 255)
    private String recipient_address;

    @Column(nullable = true, length = 50)
    private String payment_method;

    @Column(nullable = true, length = 100)
    private String memo;

    @Column(nullable = false)
    private String discount_info;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();


    @Builder
   private Order (User user, OrderSaveDto orderSaveDto, List<CartProduct> cartProducts) {
        this.orderId = UUID.randomUUID().toString();
        this.user = user;
        this.name = cartProducts.get(0).getProduct().getName();
        if(cartProducts.size()>1) {
            this.name += " 외 " + (cartProducts.size() - 1) + "개";
        }
        this.order_at = LocalDateTime.now();
        this.status = OrderStatus.결제전;
        this.recipient_name = orderSaveDto.getRecipientName();
        this.recipient_address = orderSaveDto.getDeliveryAddress() + " " + orderSaveDto.getDetailAddress();
        this.total_price = CartProduct.sumPricesFromList(cartProducts);
        this.discounted_price = orderSaveDto.getDiscountedPrice();
        this.final_price = CartProduct.sumPricesFromList(cartProducts) - orderSaveDto.getDiscountedPrice();
        this.discount_info = orderSaveDto.getDiscountInfo();
        this.memo = orderSaveDto.getMemo();

   }

    public static Order of(User user, OrderSaveDto orderSaveDto,  List<CartProduct> cartProducts) {
       return Order.builder()
               .user(user)
               .orderSaveDto(orderSaveDto)
               .cartProducts(cartProducts)
               .build();
    }

    public void updateConfirm(String payment_method, String paymentKey) {
        this.payment_method = payment_method;
        this.paymentKey = paymentKey;
        this.status = OrderStatus.결제완료;
    }

    public void updateCancel() {
        this.status = OrderStatus.취소;
    }

}
