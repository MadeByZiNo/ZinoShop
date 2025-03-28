package com.JH.JhOnlineJudge.domain.user.controller;

import com.JH.JhOnlineJudge.domain.user.entity.AuthUser;
import com.JH.JhOnlineJudge.domain.user.dto.UpdateRequest;
import com.JH.JhOnlineJudge.domain.user.exception.DuplicateNicknameException;
import com.JH.JhOnlineJudge.domain.user.exception.DuplicateUsernameException;
import com.JH.JhOnlineJudge.domain.user.exception.LoginFailedException;
import com.JH.JhOnlineJudge.domain.user.entity.User;
import com.JH.JhOnlineJudge.domain.user.dto.LoginRequest;
import com.JH.JhOnlineJudge.domain.user.dto.SignUpRequest;
import com.JH.JhOnlineJudge.domain.user.service.TokenService;
import com.JH.JhOnlineJudge.domain.user.service.UserService;
import com.JH.JhOnlineJudge.domain.user.validator.SignupValidator;
import com.JH.JhOnlineJudge.common.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;
    private final SignupValidator signupValidator;

    // 중복 체크 검증기
    @InitBinder("signUpRequest")
    public void init(WebDataBinder dataBinder){
        log.info("init binder : {}",dataBinder);
        dataBinder.addValidators(signupValidator);
    }

    // 로그인 폼
    @GetMapping("/login")
    public String getLoginForm(LoginRequest loginRequest){
        return "users/login";
    }

    // 로그인
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginRequest") LoginRequest loginRequest,
                         BindingResult bindingResult, Model model, HttpServletResponse response){

        // 입력 폼 오류 체크
        if(bindingResult.hasErrors()){
            log.info("error = {}",bindingResult);
            return "users/login";
        }

        // 로그인 처리
        try {
            ConcurrentHashMap<String,String> tokenMap = userService.login(loginRequest);
            String accessToken = tokenMap.get("accessToken");
            String refreshToken = tokenMap.get("refreshToken");
            JwtUtil.addJwtToken(response, accessToken, refreshToken);

            return "redirect:/";

        } catch (LoginFailedException e){
            bindingResult.reject(null,e.getMessage());
            return "users/login";
        }
    }



    // 회원 가입 폼
    @GetMapping("/signup")
    public String getSignupForm(SignUpRequest signUpRequest){
        return "users/signup";
    }

    // 회원 가입
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("signUpRequest") SignUpRequest signUpRequest, BindingResult bindingResult, Model model){

        // 입력 폼 오류 체크
        if(bindingResult.hasErrors()){
            log.info("error = {}",bindingResult);
            return "users/signup";
        }

        // 유저 등록
        User newUser;
        try {
             newUser = userService.signup(signUpRequest);
        } catch (DuplicateUsernameException e){
            bindingResult.rejectValue("username", null, e.getMessage());
            return "users/signup";
        } catch (DuplicateNicknameException e){
            bindingResult.rejectValue("nickname", null, e.getMessage());
            return "users/signup";
        }

        log.info("회원가입 생성={}",newUser);
        model.addAttribute("nickname",signUpRequest.getNickname());
        return "redirect:/users/signup-success";
    }

    // 회원 가입 성공 화면
    @GetMapping("/signup-success")
    public String signupSuccess(){
        log.info("가입 성공");
        return "users/signupSuccess";
    }

    // 로그 아웃
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){

        Cookie accessCookie = WebUtils.getCookie(request, "accessToken");
        Cookie refreshCookie = WebUtils.getCookie(request, "refreshToken");

        if (accessCookie != null) {
            String accessToken = accessCookie.getValue();
            tokenService.deleteAuthInfo(accessToken);

            // 쿠키 삭제
            accessCookie.setMaxAge(0);
            accessCookie.setPath("/");
            response.addCookie(accessCookie);
        }

        if (refreshCookie != null) {
            String refreshToken = refreshCookie.getValue();
            Long userId = jwtUtil.getUserId(refreshToken);
            tokenService.deleteRefreshToken(userId);

            // 쿠키 삭제
            refreshCookie.setMaxAge(0);
            refreshCookie.setPath("/");
            response.addCookie(refreshCookie);
        }

        log.info("로그아웃");
        return "redirect:/";
    }

    @GetMapping("/my-page")
    public String getMyPageForm(@AuthUser Long userId,
                                Model model) {
        User user = userService.findUserById(userId);
        model.addAttribute(user);
        return "users/mypage";
    }


    @GetMapping("/update")
    public String getUpdateForm(@AuthUser Long userId,
                                Model model) {
        User user = userService.findUserById(userId);
        UpdateRequest updateRequest = UpdateRequest.of(user.getUsername(), user.getNickname(),user.getDeliveryAddress(),user.getDetailAddress());
        model.addAttribute(user);
        model.addAttribute(updateRequest);
        return "users/update";
    }

    // 개인정보 수정
       @PostMapping("/update")
       public String update(@AuthUser Long userId, @Valid @ModelAttribute("updateRequest") UpdateRequest updateRequest
                        , BindingResult bindingResult, Model model, HttpServletResponse response){
           User user = userService.findUserById(userId);
           model.addAttribute(user);
            log.info("user info: {}",user);
           // 입력 폼 오류 체크
           if(bindingResult.hasErrors()){
               log.info("error = {}",bindingResult);
               return "users/update";
           }

           try {
               ConcurrentHashMap<String,String> tokenMap = userService.update(user, updateRequest);
               String accessToken = tokenMap.get("accessToken");
               String refreshToken = tokenMap.get("refreshToken");
               JwtUtil.addJwtToken(response, accessToken, refreshToken);
               return "redirect:/users/update-success";

           } catch (DuplicateNicknameException e){
               bindingResult.rejectValue("nickname", null, e.getMessage());
               return "users/update";
           } catch (LoginFailedException e){
               bindingResult.rejectValue("currentPassword", null, e.getMessage());
               return "users/update";
           }
       }

    @GetMapping("/update-success")
    public String updateSuccess(){
        log.info("수정 성공");
        return "users/updateSuccess";
    }


}
