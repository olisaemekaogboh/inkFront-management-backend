package com.inkFront.inFront.security;

import com.inkFront.inFront.entity.Role;
import com.inkFront.inFront.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final String DEV_FALLBACK_SECRET =
            "local-development-jwt-secret-key-that-is-long-enough-for-hmac-sha";

    private final SecretKey secretKey;
    private final long accessTokenValiditySeconds;
    private final long refreshTokenValiditySeconds;

    public JwtService(
            @Value("${app.security.jwt-secret:}") String jwtSecret,
            @Value("${app.security.access-token-validity-seconds:900}") long accessTokenValiditySeconds,
            @Value("${app.security.refresh-token-validity-seconds:604800}") long refreshTokenValiditySeconds
    ) {
        this.secretKey = buildSecretKey(jwtSecret);
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();

        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .map(Enum::name)
                .map(this::normalizeAuthority)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("email", user.getEmail())
                .claim("uid", user.getId())
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTokenValiditySeconds)))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(Long userId, String tokenId) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .id(tokenId)
                .claim("type", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshTokenValiditySeconds)))
                .signWith(secretKey)
                .compact();
    }

    public String generateTokenId() {
        return UUID.randomUUID().toString();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Claims extractAccessClaims(String token) {
        return parseClaims(token);
    }

    public Claims extractRefreshClaims(String token) {
        Claims claims = parseClaims(token);

        Object type = claims.get("type");
        if (type == null || !"refresh".equals(String.valueOf(type))) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        return claims;
    }

    public Instant extractRefreshExpiry(String token) {
        Date expiration = extractRefreshClaims(token).getExpiration();
        return expiration == null ? null : expiration.toInstant();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration() != null && claims.getExpiration().after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String normalizeAuthority(String role) {
        if (role == null || role.isBlank()) {
            return "ROLE_USER";
        }

        String cleanRole = role.trim().toUpperCase();
        return cleanRole.startsWith("ROLE_") ? cleanRole : "ROLE_" + cleanRole;
    }

    private SecretKey buildSecretKey(String configuredSecret) {
        if (configuredSecret != null && !configuredSecret.isBlank()) {
            try {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(configuredSecret));
            } catch (Exception ignored) {
                return Keys.hmacShaKeyFor(configuredSecret.getBytes(StandardCharsets.UTF_8));
            }
        }

        return Keys.hmacShaKeyFor(DEV_FALLBACK_SECRET.getBytes(StandardCharsets.UTF_8));
    }
}