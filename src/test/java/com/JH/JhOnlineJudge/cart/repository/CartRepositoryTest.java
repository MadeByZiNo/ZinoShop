package com.JH.JhOnlineJudge.cart.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class CartRepositoryTest {

    @Test
    public void showCart() {

        // given 유저의 id가 주어질때

        // when 장바구니 요청을 했다면

        // then 해당 유저id의 장바구니안 데이터들을 보여준다.

    }

    @Test
    public void removeProductFromCart() {

        // given 해당 유저 장바구니의 데이터(id)가 주어질때

        // when 장바구니 내에서 삭제 요청을 했다면

        // then 장바구니 안의 해당 물건을 삭제한다.

    }

}
