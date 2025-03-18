package com.JH.JhOnlineJudge.user.domain;

import com.JH.JhOnlineJudge.cart.domain.Cart;
import com.JH.JhOnlineJudge.coupon.domain.Coupon;
import com.JH.JhOnlineJudge.heart.domain.Heart;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.user.dto.UpdateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name="delivery_address", nullable = true)
    private String deliveryAddress;

    @Column(name="detail_address",nullable = true)
    private String detailAddress;

    @Column(name="reward_points",nullable = false)
    private int rewardPoints;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", unique = true)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @Version
    private Long version;

    @Builder
    public User(String username, String password, String nickname, String deliveryAddress, String detailAddress, int rewardPoints, UserRole role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.deliveryAddress = deliveryAddress;
        this.detailAddress = detailAddress;
        this.rewardPoints = rewardPoints;
        this.role = role;
    }


    public static User of(String username, String password, String nickname, String deliveryAddress, String detailAddress, int rewardPoints, UserRole role) {
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .deliveryAddress(deliveryAddress)
                .detailAddress(detailAddress)
                .rewardPoints(rewardPoints)
                .role(role)
                .build();
    }

    public void update(UpdateRequest updateDto) {
        this.nickname = updateDto.getNickname();
        this.deliveryAddress = updateDto.getDeliveryAddress();
        this.detailAddress = updateDto.getDetailAddress();
    }

    public void updateWithPassword(UpdateRequest updateDto) {
          this.nickname = updateDto.getNickname();
          this.deliveryAddress = updateDto.getDeliveryAddress();
          this.detailAddress = updateDto.getDetailAddress();
          this.password = updateDto.getNewPassword();
      }

      public int updateRewardPoints(Order order){
            this.rewardPoints -= order.getRewardPointsDiscountPrice();
            double saleRate;
            saleRate = this.getRole() == UserRole.고객님 ? 0.15 : 0.03;
            this.rewardPoints += order.getFinalPrice() * saleRate;
            return  (int)(order.getFinalPrice() * saleRate);
      }

    public void updateRole(UserRole role){
        this.role = role;
    }
    public void attachCart(Cart cart){
        this.cart = cart;
    }

}
