package com.inkFront.inFront.security;

import com.inkFront.inFront.entity.Role;
import com.inkFront.inFront.entity.User;
import com.inkFront.inFront.service.OAuthUserProvisioningService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuthUserProvisioningService oauthUserProvisioningService;

    public CustomOAuth2UserService(OAuthUserProvisioningService oauthUserProvisioningService) {
        this.oauthUserProvisioningService = oauthUserProvisioningService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        User user = oauthUserProvisioningService.provisionGoogleUser(oauth2User);

        Set<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(Role::getName)
                .map(Enum::name)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());

        return new DefaultOAuth2User(
                authorities,
                oauth2User.getAttributes(),
                "email"
        );
    }
}