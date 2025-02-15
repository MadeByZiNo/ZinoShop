package com.JH.JhOnlineJudge.order.OrderProduct;

import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.product.domain.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int price;

    @Builder
    private OrderProduct (Order order, Product product, int quantity, int price) {
          this.order = order;
          this.product = product;
          this.quantity = quantity;
          this.price = price;
      }


      public static OrderProduct of (Order order, Product product, int quantity, int price) {
        return OrderProduct.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .price(price)
                .build();
      }

    public void attachOrder(Order order) {
            this.order = order; // Order 필드 설정
            order.getOrderProducts().add(this);
    }
}
