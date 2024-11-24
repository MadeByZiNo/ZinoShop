package com.JH.JhOnlineJudge.product.controller;

import com.JH.JhOnlineJudge.category.CategoryService;
import com.JH.JhOnlineJudge.common.Image.ProductImage.ProductImage;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.dto.ProductDto;
import com.JH.JhOnlineJudge.product.service.ProductService;
import com.JH.JhOnlineJudge.user.admin.domain.Admin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/{productId}")
    @ResponseBody
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId) {
        ProductDto response = ProductDto.from(productService.findProductById(productId));
        return ResponseEntity.ok(response);
    }

    // 제품 등록
      @PostMapping
      @ResponseBody
      public  ResponseEntity<Map<String,String>> createProduct(@Admin Long userId,
                                                               @ModelAttribute ProductDto request,
                                  @RequestParam(required = false) MultipartFile[] images) {
         productService.createProduct(request,images);
          Map<String, String> response = new HashMap<>();
        response.put("message", "상품이 성공적으로 저장되었습니다.");
        return ResponseEntity.ok(response);
      }

    @PutMapping
    @ResponseBody
    public ResponseEntity<Map<String,String>> updateProduct(@Admin Long userId,
                                                            @ModelAttribute ProductDto request,
                                                            @RequestParam(required = false) MultipartFile[] images) {
        productService.updateProduct(request, images);
        Map<String, String> response = new HashMap<>();
        response.put("message", "상품이 성공적으로 저장되었습니다.");
        return ResponseEntity.ok(response);
    }


    @DeleteMapping
    @ResponseBody
    public ResponseEntity<Map<String,String>> deleteProduct(@Admin Long userId, @RequestParam(name = "productId") Long id) {
        productService.deleteProduct(id);
        Map<String, String> response = new HashMap<>();
       response.put("message", "상품이 성공적으로 삭제되었습니다.");
       return ResponseEntity.ok(response);
    }

    // 카테고리에 따른 상품 리스트 페이지 보여주기
    @GetMapping("/list")
    public String getProductListPage(@RequestParam(value = "category_id", required = true) Long categoryId,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                  Model model) {

        // 부모포함 모든 카테고리의 id를 가져오기
        List<Long> categoryIds = categoryService.getCategoryIdsWithParentCategory(categoryId);

        // 상품 목록 페이지 가져오기
        Page<Product> products = productService.getProductsPageByCategoryIds(categoryIds,page);


        model.addAttribute("category", categoryService.findById(categoryId));
        model.addAttribute("childCategories", categoryService.getFirstLevelChildCategories(categoryId));
        model.addAttribute("products", products);

        int totalPages = products.getTotalPages() == 0 ? 1 : products.getTotalPages();
        int currentPage = products.getNumber() + 1;
        int pageGroup = (currentPage - 1) / 5;
        int startPage = pageGroup * 5 + 1;
        int endPage = Math.min(startPage + 4, totalPages); // 5개의 페이지 또는 총 페이지까지 표시

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return "product/list";
    }


    // 제품 상세 페이지
    @GetMapping("/detail/{product_id}")
    public String getProductPage(@PathVariable(value = "product_id", required = true) Long id, Model model) {

        Product product = productService.findProductById(id);
        model.addAttribute("product", product);

        List<ProductImage> productImages = product.getImages();
        model.addAttribute("productImages",productImages);

        return "product/detail";
    }

    @GetMapping("/review/{product_id}")
    public String getReviewsPage(@PathVariable(value = "product_id", required = true) Long id, Model model) {
        Product product = productService.findProductById(id);
        model.addAttribute("product", product);
           return "product/review";
       }

}
