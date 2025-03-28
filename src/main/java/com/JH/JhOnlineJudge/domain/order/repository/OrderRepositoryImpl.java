package com.JH.JhOnlineJudge.domain.order.repository;

import com.JH.JhOnlineJudge.domain.order.entity.Order;
import com.JH.JhOnlineJudge.domain.order.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    public Slice<Order> findByStatusAndSearchText(OrderStatus status, String searchText,  Pageable pageable) {
        return orderJpaRepository.findByStatusAndSearchText(status,searchText,pageable);
    }

    @Override
    public Order findOrderWithProducts(Long orderId) {
        return orderJpaRepository.findOrderWithProducts(orderId);
    }

    public List<Order> findByUserId(Long userId) {return orderJpaRepository.findByUserId(userId);}
}
