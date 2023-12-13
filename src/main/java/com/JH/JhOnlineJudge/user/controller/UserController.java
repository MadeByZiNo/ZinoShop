package com.JH.JhOnlineJudge.user.controller;

import com.JH.JhOnlineJudge.user.domain.User;
import com.JH.JhOnlineJudge.user.dto.SignUpDto;
import com.JH.JhOnlineJudge.user.service.UserService;
import com.JH.JhOnlineJudge.user.validator.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    // 중복 체크 검증기
    @InitBinder
    public void init(WebDataBinder dataBinder){
        log.info("init binder : {}",dataBinder);
        dataBinder.addValidators(userValidator);
    }

    // 회원 가입 폼
    @GetMapping("/signup")
    public String signupForm(SignUpDto signUpDto){
        return "users/signup";
    }

    // 회원 가입
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("signUpDto") SignUpDto signUpDto, BindingResult bindingResult, Model model){

        // 중복 체크
        if(bindingResult.hasErrors()){
            log.info("error = {}",bindingResult);
            return "users/signup";
        }

        // 유저 등록
        User user = userService.signupUser(signUpDto);

        model.addAttribute("nickname",signUpDto.getNickname());
        return "redirect:/users/signupSuccess";
    }

    // 회원 가입
    @GetMapping("/signupSuccess")
    public String signupSuccess(){
        log.info("가입 성공");
        return "users/signupSuccess";
    }
}
