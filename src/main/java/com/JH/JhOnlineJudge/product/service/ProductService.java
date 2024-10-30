package com.JH.JhOnlineJudge.product.service;

import com.JH.JhOnlineJudge.category.domain.Category;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.domain.ProductImage;
import com.JH.JhOnlineJudge.product.dto.ProductCreateDto;
import com.JH.JhOnlineJudge.product.exception.NotFoundProductException;
import com.JH.JhOnlineJudge.product.repository.ProductImageRepository;
import com.JH.JhOnlineJudge.product.repository.ProductRepository;
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

    private String dirName = "Product";

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public Product findProductById(Long id) {
        Product product =  productRepository.findById(id)
                .orElseThrow(NotFoundProductException::new);
        return product;
    }

    @Transactional(rollbackFor = Exception.class)
    public Product createProduct(ProductCreateDto request, MultipartFile[] files, Category category) {

        Product product = Product.of(request,category);
        product.attachCategory(category);
        productRepository.save(product);

        List<ProductImage> imageList = new ArrayList<>();
        if (files != null && files.length > 0){
            for (MultipartFile file : files) {
                      String uploadUrl = s3Uploader.upload(file, dirName);

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
       public void deleteProduct(Long id) {
          Product findProduct = productRepository.findById(id)
                  .orElseThrow(NotFoundProductException::new);


          List<ProductImage> imageList = findProduct.getImages();

          if (!imageList.isEmpty()) {
              imageList.stream()
                      .map(ProductImage::getUrl)
                      .forEach(s3Uploader::deleteFile);
            productRepository.delete(findProduct);
          }
       }


    public Page<Product> getProductsPageByCategoryIds(List<Long> categoryIds, int offset) {
        Pageable pageable = PageRequest.of(offset -1, 6);
       return productRepository.findProductsByCategoryIds(categoryIds, pageable);
    }

}
