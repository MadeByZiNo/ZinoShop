package com.JH.JhOnlineJudge.domain.product.search;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument, Long> {

    @Query("{\"match\": {\"keywordtext\": {\"query\": \"*?0*\"}}}")
    List<ProductDocument> findByKeywordtext(String keyword);

    @Query("{\"match_phrase_prefix\": {\"name\": {\"query\": \"?0\"}}}")
    List<ProductDocument> findByNamePrefix(String keyword);
}
