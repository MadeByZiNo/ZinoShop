package com.JH.JhOnlineJudge.user.service;

import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.dto.SignUpDto;
import com.JH.JhOnlineJudge.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signupUser(SignUpDto signUpDto){

        String username = signUpDto.getUsername();
        String password = signUpDto.getPassword();
        String nickname = signUpDto.getNickname();
        String encodedPassword = passwordEncoder.encode(password);
        User userToSave = new User(username,encodedPassword,nickname,1);

        return userRepository.save(userToSave);
    }
}
