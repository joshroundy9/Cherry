package com.joshroundy.cherry.security;

import com.joshroundy.cherry.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

@Component
@RequiredArgsConstructor
public class UserIdHeaderInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headerUserId = request.getHeader("user-ID");
        String authHeader = request.getHeader("Authorization").replaceFirst("Bearer ", "");
        if (headerUserId == null || headerUserId.isEmpty()) {
            response.sendError(SC_FORBIDDEN, "Missing user-ID header");
            return false;
        }

        if (!tokenService.validateMatchingUserId(authHeader, Integer.parseInt(headerUserId))) {
            response.sendError(SC_FORBIDDEN, "user-ID mismatch");
            return false;
        }
        return true;
    }
}