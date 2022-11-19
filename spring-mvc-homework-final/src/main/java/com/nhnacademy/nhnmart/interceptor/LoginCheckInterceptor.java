package com.nhnacademy.nhnmart.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class LoginCheckInterceptor implements HandlerInterceptor {

    private static final String SESSION = "SESSION";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        HttpSession httpSession = request.getSession(false);
        if (Objects.isNull(httpSession) || httpSession.getAttribute(SESSION) == null) {
            response.sendRedirect("/cs/login?redirectURL=" + requestURI);
            return false;
        }

        return true;
    }
}
