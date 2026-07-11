package com.inkFront.inFront.security;

import com.inkFront.inFront.entity.Role;
import com.inkFront.inFront.entity.User;
import com.inkFront.inFront.service.OAuthUserProvisioningService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final OAuthUserProvisioningService provisioningService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {

        System.out.println("===== CustomOidcUserService CALLED =====");

        OidcUser oidcUser = super.loadUser(userRequest);

        User user = provisioningService.provisionGoogleUser(oidcUser);

        Set<SimpleGrantedAuthority> authorities =
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .map(Enum::name)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());

        return new DefaultOidcUser(
                authorities,
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "email"
        );
    }
}