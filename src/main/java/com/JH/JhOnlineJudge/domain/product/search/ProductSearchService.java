package com.JH.JhOnlineJudge.domain.product.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.JH.JhOnlineJudge.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductDocumentRepository productDocumentRepository;
    private final ProductRepository productRepository;
    private final ElasticsearchClient elasticsearchClient;
    public List<Long> searchIdWithKeyword(String keyword)  {

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index("products")  // 검색할 인덱스
                .query(q -> q
                        .match(m -> m
                                .field("keywordtext")  // 검색할 필드
                                .query(keyword)  // 검색할 키워드
                        )
                )
        );
        SearchResponse<ProductDocument> searchResponse = null;
        try {
            searchResponse = elasticsearchClient.search(searchRequest, ProductDocument.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //List<ProductDocument> documents = productDocumentRepository.findByKeywordtext(keyword);

        return searchResponse.hits().hits().stream()
                .map(hit -> hit.source())  // ProductDocument 객체 추출
                .map(ProductDocument::getId)  // 각 ProductDocument의 id 추출
                .collect(Collectors.toList());  // List<Long>으로 수집
    }
}