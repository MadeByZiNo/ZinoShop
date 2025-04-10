package com.JH.JhOnlineJudge.domain.product.repository;

import com.JH.JhOnlineJudge.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public List<Product> findAll(){return productJpaRepository.findAll();}

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        productJpaRepository.delete(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public Slice<Product> findSliceByCategoryIds(List<Long> categoryIds, Pageable pageable) {
        return productJpaRepository.findSliceByCategoryIds(categoryIds, pageable);
    }

    @Override
    public List<Product> findAllById(List<Long> ids ) {
        return productJpaRepository.findAllById(ids);
    }

    @Override
    public Slice<Product> findProductsByConditions(List<Long> categoryIds, String name, Pageable pageable){
        return productJpaRepository.findProductsByConditions(categoryIds, name, pageable);
    }

    @Override
    public Slice<Product> findSliceByIds(List<Long> ids, Pageable pageable) {
        return productJpaRepository.findSliceByIds(ids, pageable);
    }


}
