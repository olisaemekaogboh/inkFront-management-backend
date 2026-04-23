package com.inkFront.inFront.security;

import com.inkFront.inFront.config.SecurityCookieProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class CookieService {

    private final SecurityCookieProperties cookieProperties;

    public CookieService(SecurityCookieProperties cookieProperties) {
        this.cookieProperties = cookieProperties;
    }

    /**
     * Get cookie value from request
     */
    public String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }

        Optional<String> value = Arrays.stream(request.getCookies())
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();

        return value.orElse(null);
    }

    /**
     * Build access token cookie string
     */
    public String buildAccessTokenCookie(String token) {
        return buildCookie(
                cookieProperties.getAccessTokenName(),
                token,
                cookieProperties.getAccessTokenMaxAgeSeconds()
        ).toString();
    }

    /**
     * Build refresh token cookie string
     */
    public String buildRefreshTokenCookie(String token) {
        return buildCookie(
                cookieProperties.getRefreshTokenName(),
                token,
                cookieProperties.getRefreshTokenMaxAgeSeconds()
        ).toString();
    }

    /**
     * Clear access token cookie
     */
    public String clearAccessTokenCookie() {
        return buildCookie(cookieProperties.getAccessTokenName(), "", 0).toString();
    }

    /**
     * Clear refresh token cookie
     */
    public String clearRefreshTokenCookie() {
        return buildCookie(cookieProperties.getRefreshTokenName(), "", 0).toString();
    }

    /**
     * Clear a specific cookie by name and add to response
     */
    public void clearCookie(HttpServletResponse response, String cookieName) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .path(cookieProperties.getPath())
                .sameSite(cookieProperties.getSameSite())
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Add access token cookie to response
     */
    public void addAccessTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = buildResponseCookie(
                cookieProperties.getAccessTokenName(),
                token,
                cookieProperties.getAccessTokenMaxAgeSeconds()
        );
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Add refresh token cookie to response
     */
    public void addRefreshTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = buildResponseCookie(
                cookieProperties.getRefreshTokenName(),
                token,
                cookieProperties.getRefreshTokenMaxAgeSeconds()
        );
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Clear both access and refresh token cookies from response
     */
    public void clearAllAuthCookies(HttpServletResponse response) {
        clearCookie(response, cookieProperties.getAccessTokenName());
        clearCookie(response, cookieProperties.getRefreshTokenName());
    }

    /**
     * Check if request has a valid access token cookie
     */
    public boolean hasAccessToken(HttpServletRequest request) {
        String token = getCookieValue(request, cookieProperties.getAccessTokenName());
        return token != null && !token.isBlank();
    }

    /**
     * Check if request has a valid refresh token cookie
     */
    public boolean hasRefreshToken(HttpServletRequest request) {
        String token = getCookieValue(request, cookieProperties.getRefreshTokenName());
        return token != null && !token.isBlank();
    }

    /**
     * Get access token from request
     */
    public Optional<String> getAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(getCookieValue(request, cookieProperties.getAccessTokenName()))
                .filter(token -> !token.isBlank());
    }

    /**
     * Get refresh token from request
     */
    public Optional<String> getRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(getCookieValue(request, cookieProperties.getRefreshTokenName()))
                .filter(token -> !token.isBlank());
    }

    /**
     * Build ResponseCookie object
     */
    private ResponseCookie buildResponseCookie(String name, String value, int maxAgeSeconds) {
        return ResponseCookie.from(name, value)
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .path(cookieProperties.getPath())
                .sameSite(cookieProperties.getSameSite())
                .maxAge(maxAgeSeconds)
                .build();
    }

    /**
     * Build cookie string (legacy method)
     */
    private ResponseCookie buildCookie(String name, String value, int maxAgeSeconds) {
        return buildResponseCookie(name, value, maxAgeSeconds);
    }
}