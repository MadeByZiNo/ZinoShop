package com.JH.JhOnlineJudge.order.repository;

import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByExternalId(String externalId);

    @Query("SELECT o FROM Order o WHERE (:status IS NULL OR o.status = :status) " +
                   "AND (:searchText IS NULL OR " +
                   "(o.externalId LIKE %:searchText% " +
                   "OR o.name LIKE %:searchText% " +
                   "OR o.recipientName LIKE %:searchText% " +
                   "OR o.recipientAddress LIKE %:searchText%)) " +
                "ORDER BY o.orderAt DESC")
    List<Order> findByStatusAndSearchText(@Param("status") OrderStatus status,
                                          @Param("searchText") String searchText);

    List<Order> findByUserId(Long id);

}
