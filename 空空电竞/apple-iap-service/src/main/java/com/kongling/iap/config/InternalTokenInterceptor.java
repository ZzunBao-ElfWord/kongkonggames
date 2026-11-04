package com.kongling.iap.config;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class InternalTokenInterceptor implements HandlerInterceptor {
    private final byte[] expectedToken;

    public InternalTokenInterceptor(String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalStateException("apple.iap.internal-token must not be empty");
        }
        this.expectedToken = token.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String supplied = request.getHeader("X-Internal-Token");
        boolean accepted = supplied != null && MessageDigest.isEqual(
                expectedToken,
                supplied.getBytes(StandardCharsets.UTF_8)
        );
        if (accepted) return true;

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":\"UNAUTHORIZED\",\"message\":\"invalid internal token\"}");
        return false;
    }
}
