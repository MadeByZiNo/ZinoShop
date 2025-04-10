package com.JH.JhOnlineJudge.common.temp;

import com.JH.JhOnlineJudge.domain.product.entity.Product;
import com.JH.JhOnlineJudge.domain.product.repository.ProductJpaRepository;
import com.JH.JhOnlineJudge.domain.product.search.ProductIndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductIndexBatch {

    private final ProductIndexService productIndexService;
    private final ProductJpaRepository productJpaRepository;


    @Async
    public CompletableFuture<Void> processBatch(int start, int end, CountDownLatch latch) {
        int pageSize = end - start;
        int pageNumber = start / pageSize;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        log.info("start :  {}", start );
        Slice<Product> productSlice = productJpaRepository.findSliceBy(pageable);
        log.info(" getID : {}",productSlice.getContent().getFirst().getId());
        if (productSlice.isEmpty()) {
            System.out.println("No products to process for range: " + start + " to " + end);
            latch.countDown(); // 작업 완료 후 latch 카운트 감소
            return CompletableFuture.completedFuture(null);
        }

        List<Product> productList = productSlice.getContent();
        productIndexService.indexProducts(productList);

        latch.countDown(); // 작업 완료 후 latch 카운트 감소
        return CompletableFuture.completedFuture(null);
    }
}
