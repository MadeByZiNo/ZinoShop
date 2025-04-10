package com.JH.JhOnlineJudge.domain.analytics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticSearchService {

  /*  private final ElasticsearchOperations elasticsearchOperations;

    public Map<String, Long> getTodayPopularProducts() {
        LocalDate today = LocalDate.now();
        ZonedDateTime start = today.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime end = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1);

        long startMillis = start.toInstant().toEpochMilli();
        long endMillis = end.toInstant().toEpochMilli();


        Aggregation productViewAgg = Aggregation.of(a -> a
                .terms(t -> t
                        .field("productId")
                        .size(4)
                )
        );

        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .must(m -> m.term(t -> t.field("event.keyword").value("PRODUCT_VIEW")))


                        )
                )
                .withAggregation("top_4_products", productViewAgg)
                .build();

        SearchHits<ProductViewLog> searchHits = elasticsearchOperations.search(query, ProductViewLog.class);

        Map<String, Long> topProducts = ((ElasticsearchAggregations) searchHits.getAggregations())
                .get("top_4_products")
                .aggregation()
                .getAggregate()
                .lterms() // string terms
                .buckets()
                .array()
                .stream()
                .collect(Collectors.toMap(
                        bucket -> String.valueOf(bucket.key()),
                        MultiBucketBase::docCount
                ));
        System.out.println("topProducts.size() = " + topProducts.size());
        return topProducts;
    }*/
}
