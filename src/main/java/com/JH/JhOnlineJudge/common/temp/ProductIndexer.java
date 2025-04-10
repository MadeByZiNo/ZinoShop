package com.JH.JhOnlineJudge.common.temp;


/*
@Slf4j
@Component
@RequiredArgsConstructor

public class ProductIndexer implements ApplicationRunner {

    private final ProductJpaRepository productJpaRepository;
    private final ProductIndexBatch productIndexBatch;

    @EventListener(ApplicationReadyEvent.class)
    public void run(ApplicationArguments args)  {
        long totalProducts = productJpaRepository.count();
        int batchSize = 2000;
        int totalThreads = 10;
        long numBatches = (totalProducts + (batchSize * totalThreads) - 1) / (batchSize * totalThreads);

        for (int batchStart = 0; batchStart < numBatches; batchStart++) {
            CountDownLatch latch = new CountDownLatch(totalThreads);

            for (int i = 0; i < totalThreads; i++) {
                int start = (batchStart * totalThreads + i) * batchSize;
                int end = start + batchSize;

                productIndexBatch.processBatch(start, end, latch);
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                System.out.println("Batch " + (batchStart + 1) + "오류발생 : " + e);
            }
            System.out.println("Batch " + (batchStart + 1) + " 종료");

        }
    }



}
*/