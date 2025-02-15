package com.JH.JhOnlineJudge.cart.controller;

import com.JH.JhOnlineJudge.cart.dto.UpdateQuantityRequest;
import com.JH.JhOnlineJudge.cart.service.CartService;
import com.JH.JhOnlineJudge.cart.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.user.domain.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/product")
    public String addProductToCart(@AuthUser Long userId,
                                   @RequestParam Long productId,
                                   @RequestParam int quantity ){
        cartService.addProductToCart(userId, productId, quantity);
        return "redirect:/cart";
    }

    @DeleteMapping("/product")
       public String deleteProductFromCart (@AuthUser Long userId,
                                            @RequestParam Long productId) {
           cartService.removeProductFromCart(userId,productId);
           return "redirect:/cart";
    }

    @GetMapping
    public String viewCart(@AuthUser Long userId,
                          Model model) {
        List<CartProduct> cartProducts= cartService.getProductsInCart(userId);
        int totalPrice = CartProduct.sumPricesFromList(cartProducts);
        boolean isCartEmpty = cartProducts.isEmpty();
        boolean isAnyProductOutOfStock = cartProducts.stream().anyMatch(cartProduct -> cartProduct.getProduct().getState().equals("품절"));

        model.addAttribute("cartProducts",cartProducts);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("isCartEmpty", isCartEmpty);
        model.addAttribute("isAnyProductOutOfStock", isAnyProductOutOfStock);

        return "cart/list";
    }

    @PostMapping("/clear")
    public String clearProduct(@AuthUser Long userId,
                               @RequestParam Long productId) {
        cartService.clearCart(userId,productId);
        return "redirect:/cart";
    }

    @PostMapping("/product/quantity")
    @ResponseBody
    public ResponseEntity<Void> updateProductQuantityInCart(@AuthUser Long userId,
                                                            @RequestBody UpdateQuantityRequest request) {
        Long productId = request.getProductId();
        int quantity = request.getQuantity();

        cartService.updateProductQuantityInCart(userId, productId, quantity);
        return ResponseEntity.ok().build();
    }
}

