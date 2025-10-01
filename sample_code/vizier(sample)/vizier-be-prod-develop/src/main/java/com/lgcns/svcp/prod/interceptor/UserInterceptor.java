package com.lgcns.svcp.prod.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lgcns.svcp.prod.context.UserContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Spring Interceptor to capture user information from request headers.
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Retrieve the username from the header (temporary solution)
        String user = request.getHeader("X-User-Id");

        // TODO: Replace this with token-based extraction logic
        // Example:
        // String token = request.getHeader("Authorization");
        // String user = TokenUtil.extractUsername(token);

        UserContext.setCurrentUser(user); // Store user in ThreadLocal

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        UserContext.clear(); // Clear ThreadLocal after request processing
    }
}