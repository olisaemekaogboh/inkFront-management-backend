package com.inkFront.inFront.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.security.cookie")
public class SecurityCookieProperties {

    private String accessTokenName = "agency_access_token";
    private String refreshTokenName = "agency_refresh_token";
    private boolean secure = true;
    private boolean httpOnly = true;
    private String sameSite = "Lax";
    private String path = "/";
    private int accessTokenMaxAgeSeconds = 900;
    private int refreshTokenMaxAgeSeconds = 604800;

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

    public int getAccessTokenMaxAgeSeconds() {
        return accessTokenMaxAgeSeconds;
    }

    public void setAccessTokenMaxAgeSeconds(int accessTokenMaxAgeSeconds) {
        this.accessTokenMaxAgeSeconds = accessTokenMaxAgeSeconds;
    }

    public int getRefreshTokenMaxAgeSeconds() {
        return refreshTokenMaxAgeSeconds;
    }

    public void setRefreshTokenMaxAgeSeconds(int refreshTokenMaxAgeSeconds) {
        this.refreshTokenMaxAgeSeconds = refreshTokenMaxAgeSeconds;
    }
}