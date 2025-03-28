package com.JH.JhOnlineJudge.domain.order.OrderProduct;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderProductRepositoryImpl implements OrderProductRepository {
    private final OrderProductJpaRepository orderProductJpaRepository;

    public OrderProduct save(OrderProduct orderProduct) {
        return orderProductJpaRepository.save(orderProduct);
    }


}
