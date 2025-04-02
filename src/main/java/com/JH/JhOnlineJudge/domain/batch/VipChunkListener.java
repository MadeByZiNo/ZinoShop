package com.JH.JhOnlineJudge.domain.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VipChunkListener implements ChunkListener {
    private final VipProcessor vipProcessor;

    @Override
    public void beforeChunk(ChunkContext context) {
    }

    @Override
    public void afterChunk(ChunkContext context) {
        vipProcessor.clearState();
    }

    @Override public void afterChunkError(ChunkContext context) {}
}
