package com.JH.JhOnlineJudge.order.repository;

import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findByUserId(Long id);
    Optional<Order> findByExternalId(String externalId);
    List<Order> findByStatusAndSearchText(OrderStatus status, String searchText);
    Order findOrderWithProducts(Long orderId);

}
