package com.JH.JhOnlineJudge.cart.service;

import com.JH.JhOnlineJudge.cart.domain.Cart;
import com.JH.JhOnlineJudge.cart.exception.InvalidQuantityException;
import com.JH.JhOnlineJudge.cart.exception.NotFoundCartException;
import com.JH.JhOnlineJudge.cart.CartProduct.CartProduct;
import com.JH.JhOnlineJudge.cart.CartProduct.CartProductRepository;
import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.service.ProductService;
import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private final UserService userService;
    private final ProductService productService;
    private final CartProductRepository cartProductRepository;

    @Transactional
    public Cart addProductToCart(Long userId, Long productId, int quantity) {

        User user = userService.findUserById(userId);
        Product product = productService.getProductById(productId);

        Cart cart = user.getCart();

       if(cartProductRepository.existsByCartIdAndProductId(cart.getId(), productId)){
           return null; //이미 존재할시 아무 반응하지않음
       }

        CartProduct cartProduct = new CartProduct(cart, product, quantity);
        cart.getCartProducts().add(cartProduct);
        cartProductRepository.save(cartProduct);

        return cart;
    }

    @Transactional(readOnly = true)
    public List<CartProduct> getProductsInCart(Long userId) {
        User user = userService.findUserById(userId);

        Cart cart = user.getCart();
        return cartProductRepository.findByCartId(cart.getId());
    }

    @Transactional
    public Cart removeProductFromCart(Long userId,
                                      Long productId) {
        User user = userService.findUserById(userId);

        Cart cart = user.getCart();
        if (!cartProductRepository.existsByCartIdAndProductId(cart.getId(), productId)) {
               return null;
        }
        cart.getCartProducts().removeIf(cartProduct -> cartProduct.getProduct().getId().equals(productId));
        return cart;

    }

    @Transactional
     public void clearCart(Long userId,
                           Long productId) {
         User user = userService.findUserById(userId);
         Cart cart = user.getCart();
         cart.clear();
     }

    @Transactional
    public void updateProductQuantityInCart(Long userId, Long productId, int quantity) {

        if (quantity < 1 || quantity > 99) {
              throw new InvalidQuantityException();
        }

        User user = userService.findUserById(userId);
        Cart cart = user.getCart();

        CartProduct cartProduct = cartProductRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(NotFoundCartException::new);
        cartProduct.updateQuantity(quantity);
    }
}
