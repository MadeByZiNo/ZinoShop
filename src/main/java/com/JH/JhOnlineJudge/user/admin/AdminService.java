package com.JH.JhOnlineJudge.user.admin;

import com.JH.JhOnlineJudge.user.admin.statistic.ProductSalesRankDto;
import com.JH.JhOnlineJudge.user.admin.statistic.ProductSalesStatDto;
import com.JH.JhOnlineJudge.user.admin.statistic.ProductStat;
import com.JH.JhOnlineJudge.user.admin.statistic.ProductStatJpaRepository;
import com.JH.JhOnlineJudge.notification.domain.NotificationFrom;
import com.JH.JhOnlineJudge.notification.service.NotificationService;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.order.domain.OrderStatus;
import com.JH.JhOnlineJudge.order.service.OrderService;
import com.JH.JhOnlineJudge.user.admin.dto.DeliverySearchFilterDto;
import com.JH.JhOnlineJudge.user.admin.dto.DeliverySearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final int initialYear = 2023;
    @Transactional
    public List<DeliverySearchResponseDto> getDeliveries(DeliverySearchFilterDto deliverySearchFilterDto) {
        List<Order> searchOrders = orderService.searchOrdersByStatusAndText(deliverySearchFilterDto);

        return searchOrders.stream()
                .map(order -> DeliverySearchResponseDto.builder()
                        .orderId(order.getId())
                        .name(order.getName())
                        .username(order.getUser().getUsername())
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
            Order order = orderService.getOrderById(id);
            String message = order.getExternalId() + "의 주문이 " + order.getStatus() + "에서 " + status + " 상태로 변경되었습니다.";
            order.updateStatus(status);

            notificationService.sendNotificationMessage(order.getUser().getId(),message, NotificationFrom.배송);
        }
    }


    @Transactional
    public List<ProductSalesStatDto> getDailyProductSalesStatistic(Long productId, Integer year, Integer month) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        Map<LocalDate, ProductSalesStatDto> dailyStatMap = new HashMap<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dailyStatMap.put(date, new ProductSalesStatDto(0, 0, date.toString())); // 기본값 0으로 초기화
        }

        // 해당 달의 데이터를 가져온다.
        List<ProductStat> dailyStats = productStatJpaRepository.findByProductIdAndYearAndMonth(
            productId,
            year,
            month
        );

        // 일별 데이터 그대로 반환
        dailyStats.forEach(stat -> {
            LocalDate statDate = stat.getDate();
            ProductSalesStatDto statDto = dailyStatMap.get(statDate);
            statDto.setQuantity(stat.getQuantity());
            statDto.setPrice(stat.getTotalPrice());
        });

        List<ProductSalesStatDto> dailyResult = new ArrayList<>();
        dailyResult.addAll(dailyStatMap.values());
        return dailyResult;
    }

    @Transactional
    public List<ProductSalesStatDto> getMonthlyProductSalesStatistic(Long productId, Integer year) {
        List<ProductSalesStatDto> monthlyResult = new ArrayList<>();

        List<ProductStat> monthlyStats = productStatJpaRepository.findByProductIdAndYearAndMonth(
            productId,
            year,
      null
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

    @Transactional
    public List<ProductSalesStatDto> getYearlyProductSalesStatistic(Long productId) {
        List<ProductSalesStatDto> yearlyResult = new ArrayList<>();

        List<ProductStat> yearlyStats = productStatJpaRepository.findAllByProductId(
            productId
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
        for (int currentYear = initialYear; currentYear <= LocalDate.now().getYear(); currentYear++) {
            yearlyResult.add(new ProductSalesStatDto(
                yearlyQuantityMap.getOrDefault(currentYear, 0),
                yearlyPriceMap.getOrDefault(currentYear, 0),
                String.valueOf(currentYear)
            ));
        }

        return yearlyResult;
    }

    public List<ProductSalesRankDto> getDailyStatistics(Integer year, Integer month, Integer day) {
        List<ProductStat> stats = productStatJpaRepository.findDailyStats(year, month, day);
        return stats.stream()
                .map(stat -> new ProductSalesRankDto(stat.getProduct().getName(), stat.getQuantity(), stat.getTotalPrice()))
                .collect(Collectors.toList());
    }

    public List<ProductSalesRankDto> getMonthlyStatistics(Integer year, Integer month) {
        List<ProductStat> stats = productStatJpaRepository.findMonthlyStats(year, month);
        Map<String, ProductSalesRankDto> statMap = new HashMap<>();

        for (ProductStat stat : stats) {
            statMap.merge(
                    stat.getProduct().getName(),
                    new ProductSalesRankDto(stat.getProduct().getName(), stat.getQuantity(), stat.getTotalPrice()),
                    (result, curse) -> {
                        result.addQuantity(curse.getQuantity());
                        result.addPrice(curse.getPrice());
                        return result;
                    }
            );
        }

        return new ArrayList<>(statMap.values());
    }

    public List<ProductSalesRankDto> getYearlyStatistics(Integer year) {
        List<ProductStat> stats = productStatJpaRepository.findYearlyStats(year);
        Map<String, ProductSalesRankDto> statMap = new HashMap<>();

        for (ProductStat stat : stats) {
            statMap.merge(
                    stat.getProduct().getName(),
                    new ProductSalesRankDto(stat.getProduct().getName(), stat.getQuantity(), stat.getTotalPrice()),
                    (result, curse) -> {
                        result.addQuantity(curse.getQuantity());
                        result.addPrice(curse.getPrice());
                        return result;
                    }
            );
        }

        return new ArrayList<>(statMap.values());
    }
}
