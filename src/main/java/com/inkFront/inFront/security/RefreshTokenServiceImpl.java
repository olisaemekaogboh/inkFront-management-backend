package com.inkFront.inFront.security;

import com.inkFront.inFront.entity.RefreshTokenSession;
import com.inkFront.inFront.entity.User;
import com.inkFront.inFront.repository.RefreshTokenSessionRepository;
import com.inkFront.inFront.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenSessionRepository refreshTokenSessionRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public RefreshTokenServiceImpl(
            RefreshTokenSessionRepository refreshTokenSessionRepository,
            UserRepository userRepository,
            JwtService jwtService
    ) {
        this.refreshTokenSessionRepository = refreshTokenSessionRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public String issueRefreshToken(User user, HttpServletRequest request) {
        String tokenId = jwtService.generateTokenId();
        String refreshToken = jwtService.generateRefreshToken(user.getId(), tokenId);

        RefreshTokenSession session = new RefreshTokenSession();
        session.setUser(user);
        session.setTokenId(tokenId);
        session.setTokenHash(hashToken(refreshToken));
        session.setExpiresAt(jwtService.extractRefreshExpiry(refreshToken));
        session.setRevoked(false);
        session.setIpAddress(extractIpAddress(request));
        session.setUserAgent(extractUserAgent(request));

        refreshTokenSessionRepository.save(session);

        return refreshToken;
    }

    @Override
    @Transactional(readOnly = true)
    public User validateAndResolveUser(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw new InsufficientAuthenticationException("Refresh token is missing");
        }

        Claims claims = jwtService.extractRefreshClaims(rawRefreshToken);
        String tokenId = claims.getId();
        Long userId = Long.valueOf(claims.getSubject());

        RefreshTokenSession session = refreshTokenSessionRepository.findByTokenId(tokenId)
                .orElseThrow(() -> new CredentialsExpiredException("Refresh token session not found"));

        if (Boolean.TRUE.equals(session.getRevoked()) || session.isExpired()) {
            throw new CredentialsExpiredException("Refresh token is revoked or expired");
        }

        String incomingHash = hashToken(rawRefreshToken);
        if (session.getTokenHash() != null && !session.getTokenHash().equals(incomingHash)) {
            throw new CredentialsExpiredException("Refresh token does not match session");
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new CredentialsExpiredException("Refresh token user not found"));
    }

    @Override
    public String rotate(String oldRawRefreshToken, User user, HttpServletRequest request) {
        String newToken = issueRefreshToken(user, request);
        String newTokenId = jwtService.extractRefreshClaims(newToken).getId();

        if (oldRawRefreshToken != null && !oldRawRefreshToken.isBlank()) {
            Claims oldClaims = jwtService.extractRefreshClaims(oldRawRefreshToken);
            String oldTokenId = oldClaims.getId();

            refreshTokenSessionRepository.findByTokenId(oldTokenId).ifPresent(oldSession -> {
                oldSession.setRevoked(true);
                oldSession.setRevokedAt(Instant.now());
                oldSession.setReplacedByTokenId(newTokenId);
                refreshTokenSessionRepository.save(oldSession);
            });
        }

        return newToken;
    }

    @Override
    public void revokeRefreshTokenIfPresent(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            return;
        }

        try {
            Claims claims = jwtService.extractRefreshClaims(rawRefreshToken);
            String tokenId = claims.getId();

            refreshTokenSessionRepository.findByTokenId(tokenId).ifPresent(session -> {
                session.setRevoked(true);
                session.setRevokedAt(Instant.now());
                refreshTokenSessionRepository.save(session);
            });
        } catch (Exception ignored) {
        }
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to hash refresh token", ex);
        }
    }

    private String extractIpAddress(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            int commaIndex = forwarded.indexOf(",");
            return commaIndex > -1 ? forwarded.substring(0, commaIndex).trim() : forwarded.trim();
        }

        return request.getRemoteAddr();
    }

    private String extractUserAgent(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null) {
            return null;
        }

        return userAgent.length() > 700 ? userAgent.substring(0, 700) : userAgent;
    }
}