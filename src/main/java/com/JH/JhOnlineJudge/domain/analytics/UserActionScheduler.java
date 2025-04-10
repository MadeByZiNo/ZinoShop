package com.JH.JhOnlineJudge.domain.analytics;

import com.JH.JhOnlineJudge.common.utils.RedisHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserActionScheduler {

    private final RedisHelper redisHelper;

    @Scheduled(cron = "0 */5 * * * *") // 매 5분마다 실행
    public void resetPopular() {
        redisHelper.deleteData("popular:products");
    }


}
