package com.nhnacademy.nhnmart.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

import static com.nhnacademy.nhnmart.domain.RoleUser.ROLE_ADMIN;

public class RoleAdminCheckInterceptor implements HandlerInterceptor {

    private static final String SESSION = "SESSION";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession httpSession = request.getSession(false);

        if (Objects.isNull(httpSession)) {
            response.sendRedirect("/cs/login");
            return false;
        }

        if (!httpSession.getAttribute(SESSION).equals(ROLE_ADMIN.toString())) {
            response.sendRedirect("/cs");
            return false;
        }

        return true;
    }
}
