package com.JH.JhOnlineJudge.domain.user.entity;

import com.JH.JhOnlineJudge.domain.cart.entity.Cart;
import com.JH.JhOnlineJudge.domain.coupon.entity.Coupon;
import com.JH.JhOnlineJudge.domain.heart.entity.Heart;
import com.JH.JhOnlineJudge.domain.oauth.domain.OAuthProvider;
import com.JH.JhOnlineJudge.domain.order.entity.Order;
import com.JH.JhOnlineJudge.domain.user.dto.UpdateRequest;
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

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "oauth_id", length = 255, nullable = true)
    private String oauthId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private OAuthProvider provider;

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

    public User(String username, String password, String nickname, String deliveryAddress, String detailAddress, int rewardPoints, UserRole role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.deliveryAddress = deliveryAddress;
        this.detailAddress = detailAddress;
        this.rewardPoints = rewardPoints;
        this.role = role;
    }

    public User(String username, String nickname, String oauthId, OAuthProvider provider, int rewardPoints, UserRole role) {
        this.username = username;
        this.nickname = nickname;
        this.oauthId = oauthId;
        this.provider = provider;
        this.rewardPoints = rewardPoints;
        this.role = role;
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
