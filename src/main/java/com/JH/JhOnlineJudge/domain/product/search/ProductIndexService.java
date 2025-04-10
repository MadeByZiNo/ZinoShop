package com.JH.JhOnlineJudge.domain.product.search;

import com.JH.JhOnlineJudge.domain.category.entity.Category;
import com.JH.JhOnlineJudge.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductIndexService {

    private final ProductDocumentRepository productDocumentRepository;

    @Async
    public void indexProduct(Product product, Category category) {
        ProductDocument document = ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .category(category.getName())
                .keywords(List.of(product.getName())) // 리스트 형태라고 가정
                .build();

        productDocumentRepository.save(document);
    }

    @Async
    public void indexProducts(List<Product> products) {
        List<ProductDocument> documents = products.stream()
                .map(product -> ProductDocument.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .category(product.getCategory().getName())
                        .keywords(List.of(product.getName())) // 리스트 형태라고 가정
                        .build())
                .toList();

        productDocumentRepository.saveAll(documents);
    }
}