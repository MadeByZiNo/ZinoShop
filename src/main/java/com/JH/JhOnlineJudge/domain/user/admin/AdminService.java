package com.JH.JhOnlineJudge.domain.user.admin;

import com.JH.JhOnlineJudge.domain.product.statistic.ProductSalesRankDto;
import com.JH.JhOnlineJudge.domain.product.statistic.ProductSalesStatDto;
import com.JH.JhOnlineJudge.domain.product.statistic.ProductStatJpaRepository;
import com.JH.JhOnlineJudge.common.notification.entity.NotificationFrom;
import com.JH.JhOnlineJudge.common.notification.service.NotificationService;
import com.JH.JhOnlineJudge.domain.order.entity.Order;
import com.JH.JhOnlineJudge.domain.order.entity.OrderStatus;
import com.JH.JhOnlineJudge.domain.order.service.OrderService;
import com.JH.JhOnlineJudge.domain.user.admin.dto.DeliverySearchFilterDto;
import com.JH.JhOnlineJudge.domain.user.admin.dto.DeliverySearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final OrderService orderService;
    private final ProductStatJpaRepository productStatJpaRepository;
    private final NotificationService notificationService;

    private final int initialYear = 2023;
    @Transactional
    public Slice<DeliverySearchResponseDto> getDeliveries(DeliverySearchFilterDto deliverySearchFilterDto, int page, int size) {
        Slice<Order> searchOrders = orderService.searchOrdersByStatusAndText(deliverySearchFilterDto, page, size);


        List<DeliverySearchResponseDto> dtoList = searchOrders.getContent().stream()
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
                .toList();

        return new SliceImpl<>(dtoList, searchOrders.getPageable(), searchOrders.hasNext());
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


    @Transactional(readOnly = true)
    public List<ProductSalesStatDto> getDailyProductSalesStatistic(Long productId, Integer year, Integer month) {
        List<Object[]> results = productStatJpaRepository.findDailyAggregatedStats(productId, year, month);

        Map<String, ProductSalesStatDto> dailyStatMap = new HashMap<>();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // 날짜별 기본 값 세팅
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dailyStatMap.put(date.toString(), new ProductSalesStatDto(0, 0, date.toString()));
        }
        System.out.println("results = " +results.size());

        // 결과 채워넣기
        for (Object[] row : results) {
            LocalDate date = ((Date) row[0]).toLocalDate();
            int quantity = ((Number) row[1]).intValue();
            int price = ((Number) row[2]).intValue();
            dailyStatMap.put(date.toString(), new ProductSalesStatDto(quantity, price, date.toString()));
        }

        return new ArrayList<>(dailyStatMap.values());
    }


    @Transactional(readOnly = true)
    public List<ProductSalesStatDto> getMonthlyProductSalesStatistic(Long productId, Integer year) {
        List<Object[]> results = productStatJpaRepository.findMonthlyAggregatedStats(productId, year);

        Map<Integer, ProductSalesStatDto> monthlyStatMap = new HashMap<>();
        // 월별 기본값
        for (int month = 1; month <= 12; month++) {
            monthlyStatMap.put(month, new ProductSalesStatDto(0, 0, year + "-" + month));
        }

        for (Object[] row : results) {
            int month = ((Number) row[1]).intValue();
            int quantity = ((Number) row[2]).intValue();
            int price = ((Number) row[3]).intValue();
            monthlyStatMap.put(month, new ProductSalesStatDto(quantity, price, year + "-" + month));
        }

        return new ArrayList<>(monthlyStatMap.values());
    }

    @Transactional(readOnly = true)
    public List<ProductSalesStatDto> getYearlyProductSalesStatistic(Long productId) {
        List<Object[]> results = productStatJpaRepository.findYearlyAggregatedStats(productId);

        Map<Integer, ProductSalesStatDto> yearlyStatMap = new HashMap<>();
        int currentYear = LocalDate.now().getYear();

        for (int year = initialYear; year <= currentYear; year++) {
            yearlyStatMap.put(year, new ProductSalesStatDto(0, 0, String.valueOf(year)));
        }

        for (Object[] row : results) {
            int year = ((Number) row[0]).intValue();
            int quantity = ((Number) row[1]).intValue();
            int price = ((Number) row[2]).intValue();
            yearlyStatMap.put(year, new ProductSalesStatDto(quantity, price, String.valueOf(year)));
        }

        return new ArrayList<>(yearlyStatMap.values());
    }



    public List<ProductSalesRankDto> getDailyStatistics(Integer year, Integer month, Integer day, String sortType) {
        LocalDate date = LocalDate.of(year, month, day);
        Pageable pageable = PageRequest.of(0, 30);

        if (sortType.equals("price")) {
            return productStatJpaRepository.getTop30ByPrice(date, date, pageable);
        } else {
            return productStatJpaRepository.getTop30ByQuantity(date, date, pageable);
        }
    }

    public List<ProductSalesRankDto> getMonthlyStatistics(Integer year, Integer month, String sortType) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        Pageable pageable = PageRequest.of(0, 30);

        if (sortType.equals("price")) {
            return productStatJpaRepository.getTop30ByPrice(startDate, endDate, pageable);
        } else {
            return productStatJpaRepository.getTop30ByQuantity(startDate, endDate, pageable);
        }
    }

    public List<ProductSalesRankDto> getYearlyStatistics(Integer year, String sortType) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        Pageable pageable = PageRequest.of(0, 30);

        if (sortType.equals("price")) {
            return productStatJpaRepository.getTop30ByPrice(startDate, endDate, pageable);
        } else {
            return productStatJpaRepository.getTop30ByQuantity(startDate, endDate, pageable);
        }
    }

}
