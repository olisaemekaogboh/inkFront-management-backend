package com.inkFront.inFront.service;

import com.inkFront.inFront.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuthUserProvisioningService {
    User provisionGoogleUser(OAuth2User oAuth2User);
}