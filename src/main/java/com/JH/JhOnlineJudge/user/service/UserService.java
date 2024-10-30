package com.JH.JhOnlineJudge.user.service;

import com.JH.JhOnlineJudge.cart.domain.Cart;
import com.JH.JhOnlineJudge.cart.repository.CartRepository;
import com.JH.JhOnlineJudge.common.CartProduct.CartProductRepository;
import com.JH.JhOnlineJudge.order.domain.Order;
import com.JH.JhOnlineJudge.user.domain.UserRole;
import com.JH.JhOnlineJudge.user.dto.UpdateDto;
import com.JH.JhOnlineJudge.user.exception.DuplicateNicknameException;
import com.JH.JhOnlineJudge.user.exception.DuplicateUsernameException;
import com.JH.JhOnlineJudge.user.exception.LoginFailedException;
import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.dto.LoginDto;
import com.JH.JhOnlineJudge.user.dto.SignUpDto;
import com.JH.JhOnlineJudge.user.exception.NotFoundUserException;
import com.JH.JhOnlineJudge.user.repository.UserRepository;
import com.JH.JhOnlineJudge.utils.JwtUtil;
import jakarta.persistence.RollbackException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public User findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotFoundUserException::new);
        return user;
    }

    @Transactional
    public User signup(SignUpDto signUpDto){

        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new DuplicateUsernameException();
        }

        if (userRepository.existsByNickname(signUpDto.getNickname())) {
            throw new DuplicateNicknameException();
        }

        return registerUser(signUpDto);
    }


    @Transactional
    public ConcurrentHashMap login(LoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new LoginFailedException());

        /*if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
               throw new LoginFailedException();
        }*/

        if (!loginDto.getPassword().equals(user.getPassword())) {
                       throw new LoginFailedException();
        }

        User loginUser = userRepository.findByUsername(loginDto.getUsername()).get();
        log.info("로그인 성공 : {}",loginUser);

        ConcurrentHashMap<String,String> tokenMap = new ConcurrentHashMap();
        String accessToken = jwtUtil.generateAccessToken(loginUser.getUsername(),loginUser.getNickname());
        String refreshToken = jwtUtil.generateRefreshToken(loginUser.getUsername(),loginUser.getNickname());
        tokenMap.put("accessToken",accessToken);
        tokenMap.put("refreshToken",refreshToken);
        return tokenMap;
    }

    @Transactional(rollbackFor = Exception.class)
    public ConcurrentHashMap update(User user, UpdateDto updateDto) {

        if (!user.getNickname().equals(updateDto.getNickname()) && userRepository.existsByNickname(updateDto.getNickname())) {
                   throw new DuplicateNicknameException();
        }

        /*if(!passwordEncoder.matches(updateDto.getCurrentPassword(), user.getPassword())) {
            throw new LoginFailedException("비밀번호가 올바르지 않습니다.");
        }*/

        log.info("{}{}",updateDto.getCurrentPassword(),user.getPassword());
        if(!updateDto.getCurrentPassword().equals(user.getPassword())) {
            throw new LoginFailedException("비밀번호가 올바르지 않습니다.");
        }

        if (updateDto.getNewPassword().isEmpty()) {
            user.update(updateDto);
        } else {
            user.updateWithPassword(updateDto);
        }


        ConcurrentHashMap<String,String> tokenMap = new ConcurrentHashMap();
        String accessToken = jwtUtil.generateAccessToken(user.getUsername(),user.getNickname());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(),user.getNickname());
        tokenMap.put("accessToken",accessToken);
        tokenMap.put("refreshToken",refreshToken);

        return tokenMap;
    }

    @Transactional
    public int updateRewardPoints(Long userId, Order order) {
        User userById = findUserById(userId);
        return userById.updateRewardPoints(order);
    }

    @Transactional
    public User registerUser(SignUpDto signUpDto) {
        String username = signUpDto.getUsername();
        String password = signUpDto.getPassword();
        String nickname = signUpDto.getNickname();
        String encodedPassword = passwordEncoder.encode(password);
        String deliveryAddress = signUpDto.getDeliveryAddress();
        String detailAddress = signUpDto.getDetailAddress();
        User user = User.of(username,encodedPassword,nickname,deliveryAddress,detailAddress,0, UserRole.고객님);

        Cart cart = new Cart();
        cart.attachUser(user);
        cartRepository.save(cart);

        User saveUser = userRepository.save(user);
        return saveUser;
    }

}
