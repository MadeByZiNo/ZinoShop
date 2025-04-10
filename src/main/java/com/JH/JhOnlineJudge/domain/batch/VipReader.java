package com.JH.JhOnlineJudge.domain.batch;

import com.JH.JhOnlineJudge.common.utils.RedisHelper;
import com.JH.JhOnlineJudge.domain.user.entity.User;
import com.JH.JhOnlineJudge.domain.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@StepScope
@RequiredArgsConstructor
public class VipReader implements ItemReader<User>, ItemStream {

    private final UserJpaRepository userRepository;
    private final RedisHelper redisHelper;

    private List<User> currentChunk = new ArrayList<>();
    private int currentIndex = 0;
    private Long lastId = 0L;
    private final int chunkSize = 10000;

    @Override
    public User read() {
        // 현재 chunk 다 읽었으면 새로 가져오기
        if (currentIndex >= currentChunk.size()) {
            currentChunk = userRepository.findNextChunkByLastIdNative(lastId, chunkSize);
            currentIndex = 0;

            if (currentChunk.isEmpty()) {
                return null;
            }

            // Redis에 이번 청크 ID 저장 (Processor가 이거 보고 한 번만 쿼리하게 함)
            List<Long> userIds = currentChunk.stream()
                    .map(User::getId)
                    .toList();
            redisHelper.deleteData("batch:vip:userIds" );
            redisHelper.saveListData("batch:vip:userIds", userIds, Duration.ofMinutes(10));

            // 다음 청크를 위한 마지막 ID 저장
            lastId = currentChunk.get(currentChunk.size() - 1).getId();
            System.out.println("lastId = " + lastId);
        }

        return currentChunk.get(currentIndex++);
    }

    @Override
    public void open(ExecutionContext executionContext) {}

    @Override public void update(ExecutionContext executionContext) {}

    @Override public void close() {}
}
