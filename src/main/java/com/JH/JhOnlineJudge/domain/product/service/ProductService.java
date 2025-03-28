package com.JH.JhOnlineJudge.domain.product.service;

import com.JH.JhOnlineJudge.domain.category.CategoryService;
import com.JH.JhOnlineJudge.domain.category.entity.Category;
import com.JH.JhOnlineJudge.domain.Image.ProductImage.ProductImage;
import com.JH.JhOnlineJudge.domain.Image.ProductImage.ProductImageRepository;
import com.JH.JhOnlineJudge.domain.product.entity.Product;
import com.JH.JhOnlineJudge.domain.product.dto.ProductDto;
import com.JH.JhOnlineJudge.domain.product.dto.ProductListResponse;
import com.JH.JhOnlineJudge.domain.product.exception.NotFoundProductException;
import com.JH.JhOnlineJudge.domain.product.repository.ProductRepository;
import com.JH.JhOnlineJudge.common.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final String DIR_NAME = "Product";

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryService categoryService;
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        Product product =  productRepository.findById(id)
                .orElseThrow(NotFoundProductException::new);
        return product;
    }

    public Slice<Product> searchProducts(List<Long> categoryIds, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        return productRepository.findProductsByConditions(categoryIds, name, pageable);
    }


    @Transactional(rollbackFor = Exception.class)
    public Product createProduct(ProductDto request, MultipartFile[] images) {

        // 기본 데이터 생성
        Product product = Product.of(request);

        // 카테고리 설정
        Category category = categoryService.getCategoryById(request.getCategoryId());
        product.attachCategory(category);

        // 저장
        productRepository.save(product);

        // 이미지 설정
        List<ProductImage> imageList = new ArrayList<>();
        if (images != null){
            for (MultipartFile file : images) {
                      String uploadUrl = s3Uploader.upload(file, DIR_NAME);

                      ProductImage productImage = ProductImage.builder()
                              .url(uploadUrl)
                              .product(product)
                              .build();

                      imageList.add(productImage);
                  }
            productImageRepository.saveAll(imageList);
        }
        return product;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(ProductDto productDto, MultipartFile[] images) {

        Product product = getProductById(productDto.getId());

        // 기본데이터 수정
        product.updateProduct(productDto);

        // 카테고리 수정
        if(product.getCategory().getId() != productDto.getCategoryId()) {
            product.getCategory().removeProduct(product);
            Category category = categoryService.getCategoryById(productDto.getCategoryId());
            product.attachCategory(category);
        }

        // 이미지 수정
        if(images != null) {
            List<ProductImage> imageList = new ArrayList(product.getImages());
            imageList.stream().forEach(image -> {
              s3Uploader.deleteFile(image.getUrl());
              product.removeImage(image);
              productImageRepository.delete(image);
            });

            imageList.clear();

             for (MultipartFile file : images) {
                String uploadUrl = s3Uploader.upload(file, DIR_NAME);

                ProductImage productImage = ProductImage.builder()
                       .url(uploadUrl)
                       .product(product)
                       .build();

                imageList.add(productImage);
            }
             productImageRepository.saveAll(imageList);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
              .orElseThrow(NotFoundProductException::new);

        // 이미지 제거
        List<ProductImage> imageList = new ArrayList(product.getImages());
        imageList.stream().forEach(image -> s3Uploader.deleteFile(image.getUrl()));

        // 카테고리에서 삭제
        product.getCategory().removeProduct(product);

        // 상품 삭제
        productRepository.delete(product);

    }

    /*
    @Transactional(readOnly = true)
    public Page<ProductListResponse> getProductsPageByCategoryIds(List<Long> categoryIds, int page) {
        Pageable pageable = PageRequest.of(page - 1, 6, Sort.by("id").ascending());

        Page<Product> products = productRepository.getProductsPageWithImages(categoryIds, pageable);

        return products.map(ProductListResponse::from);
    }*/

    public Slice<ProductListResponse> getProductSliceByCategoryIds(List<Long> categoryIds, int page, int size) {
        Pageable pageable = PageRequest.of(page, size + 1, Sort.by(Sort.Direction.ASC, "id"));

        List<Product> productEntities = productRepository.findSliceByCategoryIds(categoryIds, pageable);

        boolean hasNext = productEntities.size() > size;
        if (hasNext) {
            productEntities.remove(productEntities.size() - 1); // 초과분 제거
        }

        List<ProductListResponse> dtoList = productEntities.stream()
                .map(ProductListResponse::from)
                .toList();

        return new SliceImpl<>(dtoList, pageable, hasNext);
    }


}
