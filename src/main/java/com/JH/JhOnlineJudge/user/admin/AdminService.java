package com.JH.JhOnlineJudge.user.admin;

import com.JH.JhOnlineJudge.common.statistic.ProductSalesStatDto;
import com.JH.JhOnlineJudge.common.statistic.ProductStat;
import com.JH.JhOnlineJudge.common.statistic.ProductStatJpaRepository;
import com.JH.JhOnlineJudge.notification.NotificationFrom;
import com.JH.JhOnlineJudge.notification.NotificationService;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import com.JH.JhOnlineJudge.order.service.OrderService;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.service.ProductService;
import com.JH.JhOnlineJudge.user.admin.dto.DeliverySearchFilterDto;
import com.JH.JhOnlineJudge.user.admin.dto.DeliverySearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final OrderService orderService;
    private final ProductStatJpaRepository productStatJpaRepository;
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



    private List<ProductSalesStatDto> getDailyProductSalesStatistic(Long productId, int year, int month) {
        List<ProductSalesStatDto> dailyResult = new ArrayList<>();

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<ProductStat> dailyStats = productStatJpaRepository.findByProductIdAndDateBetween(
            productId,
            startDate,
            endDate
        );

        // 일별 데이터 그대로 반환
        dailyStats.forEach(stat -> {
            dailyResult.add(new ProductSalesStatDto(
                stat.getQuantity(),
                stat.getTotalPrice(),
                stat.getDate().toString()
            ));
        });

        return dailyResult;
    }

    private List<ProductSalesStatDto> getMonthlyProductSalesStatistic(Long productId, int year) {
        List<ProductSalesStatDto> monthlyResult = new ArrayList<>();

        List<ProductStat> monthlyStats = productStatJpaRepository.findByProductIdAndYear(
            productId,
            year
        );

        // 월별 데이터 합산
        Map<Integer, Integer> monthlyQuantityMap = new HashMap<>();
        Map<Integer, Integer> monthlyPriceMap = new HashMap<>();

        for (ProductStat stat : monthlyStats) {
            int monthOfYear = stat.getDate().getMonthValue(); // 해당 월
            monthlyQuantityMap.put(monthOfYear, monthlyQuantityMap.getOrDefault(monthOfYear, 0) + stat.getQuantity());
            monthlyPriceMap.put(monthOfYear, monthlyPriceMap.getOrDefault(monthOfYear, 0) + stat.getTotalPrice());
        }

        // 월별 통계 반환
        for (int i = 1; i <= 12; i++) {
            monthlyResult.add(new ProductSalesStatDto(
                monthlyQuantityMap.getOrDefault(i, 0),
                monthlyPriceMap.getOrDefault(i, 0),
                year + "-" + i
            ));
        }

        return monthlyResult;
    }

    private List<ProductSalesStatDto> getYearlyProductSalesStatistic(Long productId, int year, Product product) {
        List<ProductSalesStatDto> yearlyResult = new ArrayList<>();

        List<ProductStat> yearlyStats = productStatJpaRepository.findByProductIdAndYearGreaterThanEqual(
            productId,
            year
        );

        // 년별 데이터 합산
        Map<Integer, Integer> yearlyQuantityMap = new HashMap<>();
        Map<Integer, Integer> yearlyPriceMap = new HashMap<>();

        for (ProductStat stat : yearlyStats) {
            int yearOfStat = stat.getDate().getYear();
            yearlyQuantityMap.put(yearOfStat, yearlyQuantityMap.getOrDefault(yearOfStat, 0) + stat.getQuantity());
            yearlyPriceMap.put(yearOfStat, yearlyPriceMap.getOrDefault(yearOfStat, 0) + stat.getTotalPrice());
        }

        // 년별 통계 반환
        for (int currentYear = year; currentYear <= LocalDate.now().getYear(); currentYear++) {
            yearlyResult.add(new ProductSalesStatDto(
                yearlyQuantityMap.getOrDefault(currentYear, 0),
                yearlyPriceMap.getOrDefault(currentYear, 0),
                String.valueOf(currentYear)
            ));
        }

        return yearlyResult;
    }

}
