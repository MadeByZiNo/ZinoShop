package com.JH.JhOnlineJudge.common.statistic;

import com.JH.JhOnlineJudge.product.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "product_stat")
@Getter
@NoArgsConstructor
public class ProductStat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, name = "total_price")
    private int totalPrice;

    public static ProductStat of(LocalDate date, Product product) {
        ProductStat productStat = new ProductStat();
        productStat.date = date;
        productStat.product = product;
        productStat.quantity = 0;
        productStat.totalPrice = 0;
        return productStat;
    }
}
