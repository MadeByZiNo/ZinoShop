package com.JH.JhOnlineJudge.domain.batch;

import com.JH.JhOnlineJudge.common.utils.ObjectSerializer;
import com.JH.JhOnlineJudge.domain.order.entity.OrderStatus;
import com.JH.JhOnlineJudge.domain.order.repository.OrderJpaRepository;
import com.JH.JhOnlineJudge.domain.user.entity.User;
import com.JH.JhOnlineJudge.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStream;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@StepScope
@RequiredArgsConstructor
public class VipProcessor implements ItemProcessor<User, User>, ItemStream {

    @Setter
    private Map<Long, Long> totalPriceMap = new HashMap<>();
    private boolean loaded = false;
    private final OrderJpaRepository orderJpaRepository;
    private final ObjectSerializer objectSerializer;

    @Override
    public void open(ExecutionContext executionContext) {
    }

    @Override
    public User process(User user) {
        if (!loaded) {
            System.out.println("loaded");
            List<Long> userIds = objectSerializer.getListData("batch:vip:userIds", Long.class);
            LocalDateTime now = LocalDateTime.now().minusDays(1);
            LocalDateTime start = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
            LocalDateTime end = start.plusMonths(1).minusNanos(1);
            List<BatchPriceDto> totals = orderJpaRepository.findTotalPriceByUserIds(
                    userIds, OrderStatus.구매확정, start, end
            );
            totalPriceMap = totals.stream()
                    .collect(Collectors.toMap(BatchPriceDto::getUserId, BatchPriceDto::getTotalPrice));

            loaded = true;
        }
        System.out.println(user.getId());

        Long total = totalPriceMap.getOrDefault(user.getId(), 0L);
        if (total >= 100000 && user.getRole() != UserRole.관리자) {
            user.updateRole(UserRole.VIP고객님);
        } else if (user.getRole() != UserRole.관리자) {
            user.updateRole(UserRole.고객님);
        }

        return user;
    }

    @Override
    public void close() {
    }

    @Override public void update(ExecutionContext executionContext) {}

    public void clearState() {
        loaded = false;
        totalPriceMap.clear();
    }

}
