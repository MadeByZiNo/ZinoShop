package com.JH.JhOnlineJudge.order.repository;

import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByExternalId(String externalId);

    List<Order> findByUserId(Long id);

    List<Order> findAllByStatus(OrderStatus status);


    @Query("SELECT o FROM Order o WHERE o.deliveredAt  BETWEEN :startDate AND :endDate" +
            "AND o.status = :status")
      List<Order> findAllByDeliveredAtAndStatus(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate,
                                                @Param("status") OrderStatus status);


    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.user.id = :userId " +
            "AND o.status = :status " +
            "AND o.orderAt BETWEEN :startOfMonth AND :endOfMonth")
    Integer sumOrdersByUserAndStatusAndDate(@Param("userId") Long userId,
                                       @Param("status") OrderStatus status,
                                       @Param("startOfMonth") LocalDateTime startOfMonth,
                                       @Param("endOfMonth") LocalDateTime endOfMonth);


    @Query("SELECT o FROM Order o WHERE (:status IS NULL OR o.status = :status) " +
                   "AND (:searchText IS NULL OR " +
                   "(o.externalId LIKE %:searchText% " +
                   "OR o.name LIKE %:searchText% " +
                   "OR o.recipientName LIKE %:searchText% " +
                   "OR o.recipientAddress LIKE %:searchText%)) " +
                "ORDER BY o.orderAt DESC")
    List<Order> findByStatusAndSearchText(@Param("status") OrderStatus status,
                                          @Param("searchText") String searchText);
}
