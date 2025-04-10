package com.JH.JhOnlineJudge.domain.analytics;

import com.JH.JhOnlineJudge.common.utils.RedisHelper;
import com.JH.JhOnlineJudge.domain.product.entity.Product;
import com.JH.JhOnlineJudge.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/popular")
public class PopularProductController {

    private final ElasticSearchService elasticsearchService;
    private final ProductService productService;
    private final RedisHelper redisHelper;

    @Cacheable(value = "popularProducts", key = "'top4'", cacheManager = "cacheManager")
    @GetMapping("/today")
    public List<PopularProductResponse> getTodayPopularProducts() {
        Set<String> topProductIds = redisHelper.getRedisTemplate().opsForZSet().reverseRange("popular:products", 0, 3);

        List<Long> ids = topProductIds.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<Product> products = productService.getProductsByIds(ids);

        return products.stream()
                .map(product -> new PopularProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getImages().get(0).getUrl() // 대표 이미지 URL
                ))
                .collect(Collectors.toList());
    }

}