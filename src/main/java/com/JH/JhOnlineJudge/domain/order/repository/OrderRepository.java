package com.JH.JhOnlineJudge.domain.order.repository;

import com.JH.JhOnlineJudge.domain.order.entity.Order;
import com.JH.JhOnlineJudge.domain.order.entity.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findByUserId(Long id);
    Optional<Order> findByExternalId(String externalId);
    Slice<Order> findByStatusAndSearchText(OrderStatus status, String searchText, Pageable pageable);
    Order findOrderWithProducts(Long orderId);

}
