package com.inkFront.inFront.security;

import com.inkFront.inFront.entity.Role;
import com.inkFront.inFront.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthorizationService {

    private final UserRepository userRepository;

    public boolean isAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        boolean authorityAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> {
                    String value = authority.getAuthority();
                    return "ROLE_ADMIN".equals(value)
                            || "ADMIN".equals(value)
                            || "ROLE_SUPER_ADMIN".equals(value)
                            || "SUPER_ADMIN".equals(value);
                });

        if (authorityAdmin) {
            return true;
        }

        String email = authentication.getName();

        return userRepository.findByEmailIgnoreCase(email)
                .map(user -> user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .map(Enum::name)
                        .anyMatch(role -> role.equals("ADMIN")
                                || role.equals("ROLE_ADMIN")
                                || role.equals("SUPER_ADMIN")
                                || role.equals("ROLE_SUPER_ADMIN")))
                .orElse(false);
    }
}