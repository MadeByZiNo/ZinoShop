package com.JH.JhOnlineJudge.product.service;

import com.JH.JhOnlineJudge.category.CategoryService;
import com.JH.JhOnlineJudge.category.domain.Category;
import com.JH.JhOnlineJudge.Image.ProductImage.ProductImage;
import com.JH.JhOnlineJudge.Image.ProductImage.ProductImageRepository;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.dto.ProductDto;
import com.JH.JhOnlineJudge.product.exception.NotFoundProductException;
import com.JH.JhOnlineJudge.product.repository.ProductRepository;
import com.JH.JhOnlineJudge.common.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
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


    @Transactional(readOnly = true)
    public Page<Product> getProductsPageByCategoryIds(List<Long> categoryIds, int offset) {
        Pageable pageable = PageRequest.of(offset -1, 6);
        return productRepository.findProductsByCategoryIds(categoryIds, pageable);
    }

}
