package com.JH.JhOnlineJudge.domain.coupon.entity;

import com.JH.JhOnlineJudge.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;

    @Column(name = "discount_rate", nullable = true)
    private Double discountRate;

    @Column(name = "discount_amount", nullable = true)
    private Integer discountAmount;

    @Column(name = "min_amount")
    private int minAmount;

    @Version
    private Long version;

    @Column(nullable = false)
    private boolean used = false;

    public void updateUsed() {
        used = true;
    }
}
