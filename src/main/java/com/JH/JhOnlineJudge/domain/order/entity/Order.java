package com.JH.JhOnlineJudge.domain.order.entity;

import com.JH.JhOnlineJudge.domain.cart.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.domain.order.OrderProduct.OrderProduct;
import com.JH.JhOnlineJudge.domain.coupon.entity.Coupon;
import com.JH.JhOnlineJudge.domain.order.dto.OrderSaveRequest;
import com.JH.JhOnlineJudge.domain.user.entity.User;
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

    @Column(name="external_id", unique = true, nullable = false)
    private String externalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="order_at", nullable = false, updatable = false)
    private LocalDateTime orderAt;

    @Column(name="delivered_at", nullable = true)
    private LocalDateTime deliveredAt;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name="payment_key", nullable = true)
    private String paymentKey;

    @Column(name="total_price",nullable = false)
    private int totalPrice;

    @Column(name="discounted_price",nullable = false)
    private int discountedPrice;

    @Column(name="final_price",nullable = false)
    private int finalPrice;

    @Column(name="recipient_name",nullable = false, length = 50)
    private String recipientName;

    @Column(name="recipient_address",nullable = false, length = 255)
    private String recipientAddress;

    @Column(name="payment_method",nullable = true, length = 50)
    private String paymentMethod;

    @Column(nullable = true, length = 100)
    private String memo;

    @Column(name="discount_info",nullable = false)
    private String discountInfo;

    @Column(name="coupon_discount_price")
    private Integer couponDiscountPrice;

    @Column(name="reward_points_discount_price")
    private Integer rewardPointsDiscountPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = true)
    private Coupon coupon;

    @Builder
    private Order (User user, OrderSaveRequest orderSaveRequest, List<CartProduct> cartProducts, Coupon coupon) {
        this.externalId = UUID.randomUUID().toString();
        this.user = user;
        this.name = cartProducts.get(0).getProduct().getName();
        if(cartProducts.size()>1) {
            this.name += " 외 " + (cartProducts.size() - 1) + "개";
        }
        this.orderAt = LocalDateTime.now();
        this.status = OrderStatus.결제전;
        this.recipientName = orderSaveRequest.getRecipientName();
        this.recipientAddress = orderSaveRequest.getDeliveryAddress() + " " + orderSaveRequest.getDetailAddress();
        this.totalPrice = CartProduct.sumPricesFromList(cartProducts);
        this.discountedPrice = orderSaveRequest.getDiscountedPrice();
        this.finalPrice = CartProduct.sumPricesFromList(cartProducts) - orderSaveRequest.getDiscountedPrice();
        this.discountInfo = orderSaveRequest.getDiscountInfo();
        this.memo = orderSaveRequest.getMemo();
        this.couponDiscountPrice = orderSaveRequest.getCouponDiscountPrice();
        this.rewardPointsDiscountPrice = orderSaveRequest.getRewardPointsDiscountPrice();
        this.coupon = coupon;

   }

    public static Order of(User user, OrderSaveRequest orderSaveRequest, List<CartProduct> cartProducts, Coupon coupon) {
       return Order.builder()
               .user(user)
               .orderSaveRequest(orderSaveRequest)
               .cartProducts(cartProducts)
               .coupon(coupon)
               .build();
    }

    public void updateConfirm(String paymentMethod, String paymentKey) {
        this.paymentMethod = paymentMethod;
        this.paymentKey = paymentKey;
        this.status = OrderStatus.결제완료;
    }

    public void updateCancel() {
        this.status = OrderStatus.취소;
    }

    public void updateStatus(OrderStatus orderStatus) {
            this.status = orderStatus;
        }

}
