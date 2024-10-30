package com.JH.JhOnlineJudge.order.repository;

import com.JH.JhOnlineJudge.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository{
    private final OrderJpaRepository orderJpaRepository;

    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    public Optional<Order> findByOrderId(String orderId) {
        return orderJpaRepository.findByOrderId(orderId);
    }
}
