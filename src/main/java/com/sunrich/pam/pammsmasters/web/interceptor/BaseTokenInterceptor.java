package com.sunrich.pam.pammsmasters.web.interceptor;

import com.sunrich.pam.pammsmasters.exception.ErrorCodes;
import com.sunrich.pam.pammsmasters.exception.InvalidAccessTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor to authenticate requests for protected endpoints
 */
@Component
@Slf4j
public class BaseTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object o) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            throw new InvalidAccessTokenException(
                    ErrorCodes.INVALID_TOKEN,
                    "missing Authorization header");
        } else if (!authHeader.startsWith("Bearer ")) {
            throw new InvalidAccessTokenException(
                    ErrorCodes.INVALID_TOKEN,
                    "Authorization must start with Bearer");
        }

        return true;
    }

}
