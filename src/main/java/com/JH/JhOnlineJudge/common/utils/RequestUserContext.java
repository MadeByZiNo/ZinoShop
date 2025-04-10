package com.JH.JhOnlineJudge.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class RequestUserContext {

    public static String getUsername() {
        if(isNotLoggedIn()) { return null;}
        return (String) getCurrentRequest().getAttribute("username");
    }

    public static String getNickname() {
        if(isNotLoggedIn()) { return null;}
        return (String) getCurrentRequest().getAttribute("nickname");
    }

    public static Long getUserId() {
        if(isNotLoggedIn()) { return null;}
        Object id = getCurrentRequest().getAttribute("userId");
        return (id instanceof Long) ? (Long) id : null;
    }

    public static boolean isNotLoggedIn() {
        HttpServletRequest req = getCurrentRequest();
        String status = (String) req.getAttribute("userStatus");
        return "NOT_LOGIN".equals(status);
    }

    private static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr == null) {return null;}
        return attr.getRequest();
    }
}
