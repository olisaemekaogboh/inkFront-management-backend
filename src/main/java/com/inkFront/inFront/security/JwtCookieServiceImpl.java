package com.inkFront.inFront.security;

import com.inkFront.inFront.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class JwtCookieServiceImpl implements JwtCookieService {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final JwtCookieProperties properties;

    public JwtCookieServiceImpl(
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            JwtCookieProperties properties
    ) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.properties = properties;
    }

    @Override
    public void writeLoginCookies(
            HttpServletRequest request,
            HttpServletResponse response,
            User user
    ) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.issueRefreshToken(user, request);

        addCookie(
                response,
                properties.getAccessTokenName(),
                accessToken,
                properties.getAccessTokenMaxAgeSeconds()
        );

        addCookie(
                response,
                properties.getRefreshTokenName(),
                refreshToken,
                properties.getRefreshTokenMaxAgeSeconds()
        );
    }

    @Override
    public void clearAuthCookies(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = extractRefreshToken(request);
        refreshTokenService.revokeRefreshTokenIfPresent(refreshToken);

        deleteCookie(response, properties.getAccessTokenName());
        deleteCookie(response, properties.getRefreshTokenName());
    }

    @Override
    public String extractAccessToken(HttpServletRequest request) {
        return extractCookieValue(request, properties.getAccessTokenName());
    }

    @Override
    public String extractRefreshToken(HttpServletRequest request) {
        return extractCookieValue(request, properties.getRefreshTokenName());
    }

    private String extractCookieValue(HttpServletRequest request, String cookieName) {
        if (request == null || request.getCookies() == null || cookieName == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    private void addCookie(
            HttpServletResponse response,
            String name,
            String value,
            long maxAgeSeconds
    ) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(properties.isHttpOnly())
                .secure(properties.isSecure())
                .sameSite(properties.getSameSite())
                .path(properties.getPath())
                .maxAge(maxAgeSeconds)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(properties.isHttpOnly())
                .secure(properties.isSecure())
                .sameSite(properties.getSameSite())
                .path(properties.getPath())
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}