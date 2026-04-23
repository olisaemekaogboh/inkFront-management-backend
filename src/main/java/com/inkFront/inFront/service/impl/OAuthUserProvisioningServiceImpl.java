package com.inkFront.inFront.service.impl;

import com.inkFront.inFront.entity.Role;
import com.inkFront.inFront.entity.User;
import com.inkFront.inFront.entity.enums.SystemRole;
import com.inkFront.inFront.exception.ResourceNotFoundException;
import com.inkFront.inFront.repository.RoleRepository;
import com.inkFront.inFront.repository.UserRepository;
import com.inkFront.inFront.service.OAuthUserProvisioningService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class OAuthUserProvisioningServiceImpl implements OAuthUserProvisioningService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public OAuthUserProvisioningServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User provisionGoogleUser(OAuth2User oAuth2User) {
        String email = normalizeEmail((String) oAuth2User.getAttributes().get("email"));
        if (email == null || email.isBlank()) {
            throw new UsernameNotFoundException("Google account email is missing");
        }

        User existingUser = userRepository.findByEmailIgnoreCase(email).orElse(null);
        if (existingUser != null) {
            if (!Boolean.TRUE.equals(existingUser.getEnabled())) {
                existingUser.setEnabled(true);
            }
            if (!Boolean.TRUE.equals(existingUser.getAccountNonLocked())) {
                existingUser.setAccountNonLocked(true);
            }
            return userRepository.save(existingUser);
        }

        Role defaultRole = roleRepository.findByName(SystemRole.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        String firstName = valueOrFallback(
                (String) oAuth2User.getAttributes().get("given_name"),
                extractFirstName((String) oAuth2User.getAttributes().get("name")),
                "Google"
        );

        String lastName = valueOrFallback(
                (String) oAuth2User.getAttributes().get("family_name"),
                extractLastName((String) oAuth2User.getAttributes().get("name")),
                "User"
        );

        String usernameBase = deriveUsernameBase(email, firstName, lastName);
        String username = ensureUniqueUsername(usernameBase);

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setEnabled(true);
        user.setAccountNonLocked(true);

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private String valueOrFallback(String primary, String secondary, String fallback) {
        if (primary != null && !primary.isBlank()) {
            return primary.trim();
        }
        if (secondary != null && !secondary.isBlank()) {
            return secondary.trim();
        }
        return fallback;
    }

    private String extractFirstName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return null;
        }
        String[] parts = fullName.trim().split("\\s+");
        return parts.length > 0 ? parts[0] : null;
    }

    private String extractLastName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return null;
        }
        String[] parts = fullName.trim().split("\\s+");
        return parts.length > 1 ? parts[parts.length - 1] : "User";
    }

    private String deriveUsernameBase(String email, String firstName, String lastName) {
        String localPart = email != null && email.contains("@")
                ? email.substring(0, email.indexOf('@'))
                : null;

        String candidate = localPart;
        if (candidate == null || candidate.isBlank()) {
            candidate = (firstName + "." + lastName).toLowerCase();
        }

        candidate = candidate
                .toLowerCase()
                .replaceAll("[^a-z0-9._-]", "")
                .replaceAll("^[._-]+|[._-]+$", "");

        if (candidate.isBlank()) {
            candidate = "user";
        }

        if (candidate.length() > 80) {
            candidate = candidate.substring(0, 80);
        }

        return candidate;
    }

    private String ensureUniqueUsername(String base) {
        String candidate = base;
        int counter = 1;

        while (userRepository.existsByUsernameIgnoreCase(candidate)) {
            String suffix = String.valueOf(counter++);
            int maxBaseLength = 80 - suffix.length() - 1;
            String trimmedBase = base.length() > maxBaseLength ? base.substring(0, maxBaseLength) : base;
            candidate = trimmedBase + "_" + suffix;
        }

        return candidate;
    }
}