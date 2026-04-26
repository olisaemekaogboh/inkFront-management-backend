package com.inkFront.inFront.security;

import com.inkFront.inFront.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface RefreshTokenService {

    String issueRefreshToken(User user, HttpServletRequest request);

    User validateAndResolveUser(String rawRefreshToken);

    String rotate(String oldRawRefreshToken, User user, HttpServletRequest request);

    void revokeRefreshTokenIfPresent(String rawRefreshToken);
}