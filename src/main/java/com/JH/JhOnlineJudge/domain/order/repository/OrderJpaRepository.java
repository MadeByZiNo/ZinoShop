package com.JH.JhOnlineJudge.domain.order.repository;

import com.JH.JhOnlineJudge.domain.batch.BatchPriceDto;
import com.JH.JhOnlineJudge.domain.order.entity.Order;
import com.JH.JhOnlineJudge.domain.order.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.deliveredAt BETWEEN :startDate AND :endDate " +
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


    @Query("SELECT o FROM Order o " +
            "WHERE (:status IS NULL OR o.status = :status) " +
            "AND (:searchText IS NULL OR " +
            "(o.externalId LIKE %:searchText% " +
            "OR o.name LIKE %:searchText% " +
            "OR o.recipientName LIKE %:searchText% " +
            "OR o.recipientAddress LIKE %:searchText%))")
    Slice<Order> findByStatusAndSearchText(
            @Param("status") OrderStatus status,
            @Param("searchText") String searchText,
            Pageable pageable
    );


    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.orderProducts op " +
            "JOIN FETCH op.product " +
            "WHERE o.id = :orderId")
    Order findOrderWithProducts(@Param("orderId") Long orderId);

    @Query("SELECT new com.JH.JhOnlineJudge.domain.batch.BatchPriceDto(o.user.id, SUM(op.price * op.quantity)) " +
            "FROM Order o " +
            "JOIN o.orderProducts op " +
            "WHERE o.user.id IN :userIds " +
            "AND o.status = :status " +
            "AND o.orderAt BETWEEN :start AND :end " +
            "GROUP BY o.user.id")
    List<BatchPriceDto> findTotalPriceByUserIds(
            @Param("userIds") List<Long> userIds,
            @Param("status") OrderStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
