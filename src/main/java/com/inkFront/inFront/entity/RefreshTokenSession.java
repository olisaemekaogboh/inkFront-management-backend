package com.inkFront.inFront.entity;

import com.inkFront.inFront.entity.base.AuditableEntity;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(
        name = "refresh_token_sessions",
        indexes = {
                @Index(name = "idx_refresh_token_sessions_user_id", columnList = "user_id"),
                @Index(name = "idx_refresh_token_sessions_token_id", columnList = "token_id"),
                @Index(name = "idx_refresh_token_sessions_expires_at", columnList = "expires_at"),
                @Index(name = "idx_refresh_token_sessions_revoked_at", columnList = "revoked_at")
        }
)
public class RefreshTokenSession extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_id", unique = true, length = 120)
    private String tokenId;

    @Column(name = "replaced_by_token_id", length = 120)
    private String replacedByTokenId;

    @Column(name = "token_hash", unique = true, length = 255)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "last_used_at")
    private Instant lastUsedAt;

    @Column(name = "ip_address", length = 80)
    private String ipAddress;

    @Column(name = "user_agent", length = 700)
    private String userAgent;

    public RefreshTokenSession() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getReplacedByTokenId() {
        return replacedByTokenId;
    }

    public void setReplacedByTokenId(String replacedByTokenId) {
        this.replacedByTokenId = replacedByTokenId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getExpiresAtAsLocalDateTime() {
        return expiresAt == null ? null : LocalDateTime.ofInstant(expiresAt, ZoneId.systemDefault());
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt == null ? null : expiresAt.atZone(ZoneId.systemDefault()).toInstant();
    }

    public boolean isRevoked() {
        return revoked;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
        this.revokedAt = revoked && this.revokedAt == null ? Instant.now() : this.revokedAt;
    }

    public void setRevoked(Boolean revoked) {
        setRevoked(revoked != null && revoked);
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
        this.revoked = revokedAt != null || this.revoked;
    }

    public void setRevokedAt(LocalDateTime revokedAt) {
        setRevokedAt(revokedAt == null ? null : revokedAt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Instant getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(Instant lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public void setLastUsedAt(LocalDateTime lastUsedAt) {
        this.lastUsedAt = lastUsedAt == null ? null : lastUsedAt.atZone(ZoneId.systemDefault()).toInstant();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    public void revoke() {
        this.revoked = true;
        this.revokedAt = Instant.now();
    }

    public void markUsed() {
        this.lastUsedAt = Instant.now();
    }
}