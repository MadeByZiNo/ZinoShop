package com.JH.JhOnlineJudge.product.controller;

import com.JH.JhOnlineJudge.category.CategoryService;
import com.JH.JhOnlineJudge.category.domain.Category;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.common.Image.ProductImage.ProductImage;
import com.JH.JhOnlineJudge.product.dto.ProductCreateDto;
import com.JH.JhOnlineJudge.product.service.ProductService;
import com.JH.JhOnlineJudge.review.dto.ReviewListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    // 제품 등록
    @PostMapping
    public String createProduct(@RequestPart(value = "request") ProductCreateDto request,
                                @RequestPart(value = "files") MultipartFile[] files) {

        Category category = categoryService.findCategory(request.getCategoryId());
        productService.createProduct(request,files,category);
        return "redirect:/";
    }

    // 제품 삭제
    @DeleteMapping
    public String deleteProduct(@RequestParam(name = "id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }

    // 카테고리에 따른 상품 리스트 페이지 보여주기
    @GetMapping("/list")
    public String getProductListPage(@RequestParam(value = "category_id", required = true) Long category_id,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                  Model model) {

        Category findCategory = categoryService.findCategory(category_id);

        List<Category> childCategories = findCategory.getAllChildCategories();
        List<Category> firstChildCategories = childCategories.stream()
                .filter(category -> category.getParent() == findCategory)
                .toList();

        // 모든 자식카테고리 포함 상품 구해오기
        List<Long> categoryIds = childCategories.stream()
                        .map(Category::getId)
                        .collect(Collectors.toList());
        categoryIds.add(category_id);

        Page<Product> products = productService.getProductsPageByCategoryIds(categoryIds,page);

        model.addAttribute("category", findCategory);
        model.addAttribute("childCategories", firstChildCategories);
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
