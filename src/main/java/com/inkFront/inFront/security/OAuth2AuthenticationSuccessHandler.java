package com.inkFront.inFront.security;

import com.inkFront.inFront.config.SecurityCookieProperties;
import com.inkFront.inFront.entity.RefreshTokenSession;
import com.inkFront.inFront.entity.User;
import com.inkFront.inFront.service.OAuthUserProvisioningService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.inkFront.inFront.repository.RefreshTokenSessionRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthUserProvisioningService oAuthUserProvisioningService;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final RefreshTokenSessionRepository refreshTokenSessionRepository;
    private final SecurityCookieProperties cookieProperties;

    @Value("${app.frontend.base-url:http://localhost:5173}")
    private String frontendBaseUrl;

    public OAuth2AuthenticationSuccessHandler(
            OAuthUserProvisioningService oAuthUserProvisioningService,
            JwtService jwtService,
            CookieService cookieService,
            RefreshTokenSessionRepository refreshTokenSessionRepository,
            SecurityCookieProperties cookieProperties
    ) {
        this.oAuthUserProvisioningService = oAuthUserProvisioningService;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.refreshTokenSessionRepository = refreshTokenSessionRepository;
        this.cookieProperties = cookieProperties;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        Object principalObject = authentication.getPrincipal();
        if (!(principalObject instanceof OAuth2User oAuth2User)) {
            response.sendRedirect(frontendBaseUrl + "/login?error=oauth_principal_invalid");
            return;
        }

        User user = oAuthUserProvisioningService.provisionGoogleUser(oAuth2User);
        UserPrincipal principal = new UserPrincipal(user);

        String accessToken = jwtService.generateAccessToken(principal);
        String refreshTokenId = jwtService.generateTokenId();
        String refreshToken = jwtService.generateRefreshToken(user.getId(), refreshTokenId);

        RefreshTokenSession refreshSession = new RefreshTokenSession();
        refreshSession.setTokenId(refreshTokenId);
        refreshSession.setTokenHash(sha256(refreshToken));
        refreshSession.setUser(user);
        refreshSession.setExpiresAt(jwtService.extractRefreshExpiry(refreshToken));
        refreshSession.setRevoked(false);
        refreshSession.setUserAgent(truncate(request.getHeader("User-Agent"), 255));
        refreshSession.setIpAddress(truncate(resolveClientIp(request), 64));
        refreshTokenSessionRepository.save(refreshSession);

        response.addHeader(HttpHeaders.SET_COOKIE, cookieService.buildAccessTokenCookie(accessToken));
        response.addHeader(HttpHeaders.SET_COOKIE, cookieService.buildRefreshTokenCookie(refreshToken));

        response.sendRedirect(frontendBaseUrl + "/admin");
    }

    private String resolveClientIp(HttpServletRequest request) {
        String header = request.getHeader("X-Forwarded-For");
        if (header != null && !header.isBlank()) {
            return header.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to hash refresh token", ex);
        }
    }
}