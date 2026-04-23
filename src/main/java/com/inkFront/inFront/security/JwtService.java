package com.inkFront.inFront.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${app.security.jwt.issuer}")
    private String issuer;

    @Value("${app.security.jwt.access-token-secret}")
    private String accessTokenSecretValue;

    @Value("${app.security.jwt.refresh-token-secret}")
    private String refreshTokenSecretValue;

    @Value("${app.security.jwt.access-token-expiration-seconds}")
    private long accessTokenExpirationSeconds;

    @Value("${app.security.jwt.refresh-token-expiration-seconds}")
    private long refreshTokenExpirationSeconds;

    private SecretKey accessTokenSecret;
    private SecretKey refreshTokenSecret;

    @PostConstruct
    public void init() {
        this.accessTokenSecret = toKey(accessTokenSecretValue);
        this.refreshTokenSecret = toKey(refreshTokenSecretValue);
    }

    public String generateAccessToken(UserPrincipal principal) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(accessTokenExpirationSeconds);

        return Jwts.builder()
                .subject(String.valueOf(principal.getId()))
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .claims(Map.of(
                        "email", principal.getUsername(),
                        "roles", principal.getAuthorities().stream().map(Object::toString).toList(),
                        "type", "access"
                ))
                .signWith(accessTokenSecret)
                .compact();
    }

    public String generateRefreshToken(Long userId, String tokenId) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(refreshTokenExpirationSeconds);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .id(tokenId)
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .claim("type", "refresh")
                .signWith(refreshTokenSecret)
                .compact();
    }

    public Claims extractAccessClaims(String token) {
        return Jwts.parser()
                .verifyWith(accessTokenSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims extractRefreshClaims(String token) {
        return Jwts.parser()
                .verifyWith(refreshTokenSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public LocalDateTime extractRefreshExpiry(String token) {
        Date expiration = extractRefreshClaims(token).getExpiration();
        return LocalDateTime.ofInstant(expiration.toInstant(), ZoneOffset.UTC);
    }

    public String generateTokenId() {
        return UUID.randomUUID().toString();
    }

    private SecretKey toKey(String value) {
        byte[] bytes;
        try {
            bytes = Decoders.BASE64.decode(value);
        } catch (Exception ex) {
            bytes = value.getBytes(StandardCharsets.UTF_8);
        }
        return Keys.hmacShaKeyFor(bytes.length >= 32 ? bytes : pad(bytes));
    }

    private byte[] pad(byte[] input) {
        byte[] padded = new byte[32];
        for (int i = 0; i < padded.length; i++) {
            padded[i] = input[i % input.length];
        }
        return padded;
    }
}