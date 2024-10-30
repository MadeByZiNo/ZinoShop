package com.JH.JhOnlineJudge.heart;


import com.JH.JhOnlineJudge.product.domain.Product;
import com.JH.JhOnlineJudge.product.service.ProductService;
import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HeartService {

    private final HeartRepository heartRepository;
    private final UserService userService;
    private final ProductService productService;

    @Transactional
    public boolean updateHeart(Long userId,
                            Long productId) {

        User user =  userService.findUserById(userId);
        Product product = productService.findProductById(productId);


        Optional<Heart> existingHeart = heartRepository.findByUserIdAndProductId(userId, productId);

        boolean isHearted = existingHeart.map(heart -> {
            heartRepository.delete(heart);
            return false;
        }).orElseGet(() -> {
            heartRepository.save(Heart.of(user, product));
            return true;
        });

        return isHearted;

    }

    public Page<Heart> getProductsPageByCategoryIds(Long userId, int offset) {

        User user =  userService.findUserById(userId);
        Pageable pageable = PageRequest.of(offset -1, 6, Sort.by(Sort.Direction.DESC, "id"));
        return heartRepository.findByUser(user, pageable);
    }

    public boolean existsHeart(Long userId,
                                Long productId) {
        return heartRepository.existsByUserIdAndProductId(userId, productId);
    }
}
