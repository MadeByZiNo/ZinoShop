package com.JH.JhOnlineJudge.order.repository;

import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository{
    private final OrderJpaRepository orderJpaRepository;

    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    public Optional<Order> findById(Long id) {return orderJpaRepository.findById(id);}

    public Optional<Order> findByExternalId(String externalId) {
        return orderJpaRepository.findByExternalId(externalId);
    }

    public List<Order> findByStatusAndSearchText(OrderStatus status, String searchText) {
        return orderJpaRepository.findByStatusAndSearchText(status,searchText);
    }
}
