package com.JH.JhOnlineJudge.user.admin;

import com.JH.JhOnlineJudge.notification.NotificationFrom;
import com.JH.JhOnlineJudge.notification.NotificationService;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import com.JH.JhOnlineJudge.order.service.OrderService;
import com.JH.JhOnlineJudge.user.admin.dto.DeliverySearchFilterDto;
import com.JH.JhOnlineJudge.user.admin.dto.DeliverySearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final OrderService orderService;
    private final NotificationService notificationService;

    @Transactional
    public List<DeliverySearchResponseDto> getDeliveries(DeliverySearchFilterDto deliverySearchFilterDto) {
        List<Order> searchOrders = orderService.findByStatusAndSearchText(deliverySearchFilterDto);

        return searchOrders.stream()
                .map(order -> DeliverySearchResponseDto.builder()
                        .orderId(order.getId())
                        .name(order.getName())
                        .username(order.getRecipientName())
                        .price(order.getFinalPrice())
                        .status(order.getStatus().toString())
                        .recipientName(order.getRecipientName())
                        .recipientAddress(order.getRecipientAddress())
                        .memo(order.getMemo())
                        .orderAt(order.getOrderAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateDeliveryStatus(List<Long> deliveryIds,OrderStatus status) {
        for (Long id : deliveryIds) {
            Order order = orderService.findById(id);
            String message = order.getExternalId() + "의 주문이 " + order.getStatus() + "에서 " + status + " 상태로 변경되었습니다.";
            order.updateStatus(status);

            notificationService.sendNotificationMessage(order.getUser().getId(),message, NotificationFrom.배송);
        }
    }
}
