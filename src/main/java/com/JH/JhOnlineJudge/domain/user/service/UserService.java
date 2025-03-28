package com.JH.JhOnlineJudge.domain.user.service;

import com.JH.JhOnlineJudge.domain.cart.entity.Cart;
import com.JH.JhOnlineJudge.domain.cart.repository.CartRepository;
import com.JH.JhOnlineJudge.domain.user.entity.UserRole;
import com.JH.JhOnlineJudge.domain.user.dto.UpdateRequest;
import com.JH.JhOnlineJudge.domain.user.exception.DuplicateNicknameException;
import com.JH.JhOnlineJudge.domain.user.exception.DuplicateUsernameException;
import com.JH.JhOnlineJudge.domain.user.exception.LoginFailedException;
import com.JH.JhOnlineJudge.domain.user.entity.User;
import com.JH.JhOnlineJudge.domain.user.dto.LoginRequest;
import com.JH.JhOnlineJudge.domain.user.dto.SignUpRequest;
import com.JH.JhOnlineJudge.domain.user.exception.NotFoundUserException;
import com.JH.JhOnlineJudge.domain.oauth.domain.OAuthUserInfo;
import com.JH.JhOnlineJudge.domain.user.repository.UserRepository;
import com.JH.JhOnlineJudge.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotFoundUserException::new);
        return user;
    }

    @Transactional
    public User signup(SignUpRequest signUpDto){

        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new DuplicateUsernameException();
        }

        if (userRepository.existsByNickname(signUpDto.getNickname())) {
            throw new DuplicateNicknameException();
        }

        return registerUser(signUpDto);
    }


    @Transactional
    public ConcurrentHashMap login(LoginRequest loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new LoginFailedException());

        /*if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
               throw new LoginFailedException();
        }*/

        if (!loginDto.getPassword().equals(user.getPassword())) {
            throw new LoginFailedException();
        }

        return tokenService.issueJwt(user);
    }


    @Transactional(rollbackFor = Exception.class)
    public ConcurrentHashMap update(User user, UpdateRequest updateDto) {

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

        ConcurrentHashMap<String, String> tokenMap= tokenService.issueJwt(user);

        return tokenMap;
    }


    @Transactional
    public User registerUser(SignUpRequest signUpDto) {
        String username = signUpDto.getUsername();
        String password = signUpDto.getPassword();
        String nickname = signUpDto.getNickname();
        String encodedPassword = passwordEncoder.encode(password);
        String deliveryAddress = signUpDto.getDeliveryAddress();
        String detailAddress = signUpDto.getDetailAddress();
        User user = new User(username,encodedPassword,nickname,deliveryAddress,detailAddress,0, UserRole.고객님);

        Cart cart = new Cart();
        cart.attachUser(user);
        cartRepository.save(cart);

        User saveUser = userRepository.save(user);
        return saveUser;
    }

    @Transactional
    public User findOrRegister(OAuthUserInfo userInfo) {
        return userRepository.findByOauthId(userInfo.getId())
                .orElseGet(() -> {
                    User newUser = new User(userInfo.getEmail(), userInfo.getNickname(), userInfo.getId(), userInfo.getProvider(), 0, UserRole.고객님);
                    Cart cart = new Cart();
                    newUser.attachCart(cart);
                    cartRepository.save(cart);
                    return userRepository.save(newUser);
                });
    }

}
