package com.JH.JhOnlineJudge.user.domain;

import com.JH.JhOnlineJudge.cart.domain.Cart;
import com.JH.JhOnlineJudge.heart.Heart;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.user.dto.UpdateDto;
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

    @Column(nullable = true)
    private String delivery_address;

    @Column(nullable = true)
    private String detail_address;

    @Column(nullable = false)
    private int reward_points;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heart> hearts = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Cart cart;

    @Builder
    public User(String username, String password, String nickname, String delivery_address, String detail_address, int reward_points, UserRole role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.delivery_address = delivery_address;
        this.detail_address = detail_address;
        this.reward_points = reward_points;
        this.role = role;
    }


    public static User of(String username, String password, String nickname, String delivery_address, String detail_address, int reward_points, UserRole role) {
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .delivery_address(delivery_address)
                .detail_address(detail_address)
                .reward_points(reward_points)
                .role(role)
                .build();
    }

    public void update(UpdateDto updateDto) {
        this.nickname = updateDto.getNickname();
        this.delivery_address = updateDto.getDeliveryAddress();
        this.detail_address = updateDto.getDetailAddress();
    }

    public void updateWithPassword(UpdateDto updateDto) {
          this.nickname = updateDto.getNickname();
          this.delivery_address = updateDto.getDeliveryAddress();
          this.detail_address = updateDto.getDetailAddress();
          this.password = updateDto.getNewPassword();
      }

      public int updateRewardPoints(Order order){
            this.reward_points -= order.getDiscounted_price();
            this.reward_points += order.getFinal_price() * 0.2;
            return  (int)(order.getFinal_price() * 0.2);
      }
    public void attachCart(Cart cart){
        this.cart = cart;
    }

}
