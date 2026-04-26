package com.inkFront.inFront.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.cookie")
public class JwtCookieProperties {

    private String accessTokenName = "agency_access_token";
    private String refreshTokenName = "agency_refresh_token";
    private boolean secure = false;
    private boolean httpOnly = true;
    private String sameSite = "Lax";
    private String path = "/";
    private long accessTokenMaxAgeSeconds = 900;
    private long refreshTokenMaxAgeSeconds = 604800;

    public String getAccessTokenName() {
        return accessTokenName;
    }

    public void setAccessTokenName(String accessTokenName) {
        this.accessTokenName = accessTokenName;
    }

    public String getRefreshTokenName() {
        return refreshTokenName;
    }

    public void setRefreshTokenName(String refreshTokenName) {
        this.refreshTokenName = refreshTokenName;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public String getSameSite() {
        return sameSite;
    }

    public void setSameSite(String sameSite) {
        this.sameSite = sameSite;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getAccessTokenMaxAgeSeconds() {
        return accessTokenMaxAgeSeconds;
    }

    public void setAccessTokenMaxAgeSeconds(long accessTokenMaxAgeSeconds) {
        this.accessTokenMaxAgeSeconds = accessTokenMaxAgeSeconds;
    }

    public long getRefreshTokenMaxAgeSeconds() {
        return refreshTokenMaxAgeSeconds;
    }

    public void setRefreshTokenMaxAgeSeconds(long refreshTokenMaxAgeSeconds) {
        this.refreshTokenMaxAgeSeconds = refreshTokenMaxAgeSeconds;
    }
}