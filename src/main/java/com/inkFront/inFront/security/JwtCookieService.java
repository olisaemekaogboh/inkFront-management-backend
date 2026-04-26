package com.inkFront.inFront.security;

import com.inkFront.inFront.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtCookieService {

    void writeLoginCookies(
            HttpServletRequest request,
            HttpServletResponse response,
            User user
    );

    void clearAuthCookies(HttpServletRequest request, HttpServletResponse response);

    String extractAccessToken(HttpServletRequest request);

    String extractRefreshToken(HttpServletRequest request);
}