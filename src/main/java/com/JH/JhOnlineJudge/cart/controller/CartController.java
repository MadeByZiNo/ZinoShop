package com.JH.JhOnlineJudge.cart.controller;

import com.JH.JhOnlineJudge.cart.domain.Cart;
import com.JH.JhOnlineJudge.cart.dto.UpdateQuantityRequest;
import com.JH.JhOnlineJudge.cart.service.CartService;
import com.JH.JhOnlineJudge.common.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.common.CartProduct.CartProductRepository;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.service.ProductService;
import com.JH.JhOnlineJudge.user.domain.AuthUser;
import com.JH.JhOnlineJudge.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-product")
    public String addProductToCart(@AuthUser Long userId,
                                   @RequestParam Long productId,
                                   @RequestParam int quantity ){
        cartService.saveProduct(userId, productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping
    public String getCartProducts(@AuthUser Long userId,
                          Model model) {
        List<CartProduct> cartProducts= cartService.getCartProductList(userId);
        int totalPrice = CartProduct.sumPricesFromList(cartProducts);

        model.addAttribute("cartProducts",cartProducts);
        model.addAttribute("totalPrice", totalPrice);
        return "cart/list";
    }

    @PostMapping("/remove")
    public String deleteProduct(@AuthUser Long userId,
                                @RequestParam Long productId) {
        cartService.deleteProduct(userId,productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
        public String clearProduct(@AuthUser Long userId,
                                    @RequestParam Long productId) {
            cartService.clearProduct(userId,productId);
            return "redirect:/cart";
        }

    @PostMapping("/update-quantity")
    @ResponseBody
    public ResponseEntity<Void> updateQuantity(@AuthUser Long userId,
            @RequestBody UpdateQuantityRequest request) {
        Long productId = request.getProductId();
        int quantity = request.getQuantity();

        cartService.updateQuantity(userId, productId, quantity);
        return ResponseEntity.ok().build();
    }
}

