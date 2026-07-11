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

        System.out.println("========== GOOGLE LOGIN ==========");

        String email = normalizeEmail((String) oAuth2User.getAttribute("email"));

        System.out.println("Email = " + email);
        System.out.println("Attributes = " + oAuth2User.getAttributes());

        if (email == null || email.isBlank()) {
            throw new UsernameNotFoundException("Google account email is missing");
        }

        User existingUser = userRepository.findByEmailIgnoreCase(email).orElse(null);

        String firstName = valueOrFallback(
                (String) oAuth2User.getAttribute("given_name"),
                extractFirstName((String) oAuth2User.getAttribute("name")),
                "Google"
        );

        String lastName = valueOrFallback(
                (String) oAuth2User.getAttribute("family_name"),
                extractLastName((String) oAuth2User.getAttribute("name")),
                "User"
        );

        String displayName = valueOrFallback(
                (String) oAuth2User.getAttribute("name"),
                firstName + " " + lastName,
                email
        );

        String avatarUrl = (String) oAuth2User.getAttribute("picture");

        if (existingUser != null) {

            System.out.println("Existing user found");

            existingUser.setFirstName(firstName);
            existingUser.setLastName(lastName);
            existingUser.setDisplayName(displayName);
            existingUser.setAvatarUrl(avatarUrl);

            existingUser.setEnabled(true);
            existingUser.setAccountNonLocked(true);
            existingUser.setEmailVerified(true);

            existingUser.setProvider(User.AuthProvider.GOOGLE);
            existingUser.setProviderUserId(oAuth2User.getName());

            // Automatically ensure your account always has ROLE_ADMIN
            if ("ogboholisa@gmail.com".equalsIgnoreCase(email)) {

                Role adminRole = roleRepository.findByName(SystemRole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

                existingUser.getRoles().add(adminRole);
            }

            User saved = userRepository.saveAndFlush(existingUser);

            System.out.println("Updated user ID = " + saved.getId());

            return saved;
        }

        System.out.println("Creating new user...");

        Role defaultRole = roleRepository.findByName(SystemRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        String usernameBase = deriveUsernameBase(email, firstName, lastName);
        String username = ensureUniqueUsername(usernameBase);

        User user = new User();

        user.setEmail(email);
        user.setUsername(username);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDisplayName(displayName);
        user.setAvatarUrl(avatarUrl);

        user.setProvider(User.AuthProvider.GOOGLE);
        user.setProviderUserId(oAuth2User.getName());

        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setAccountNonLocked(true);

        user.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));

        // Every user gets ROLE_USER
        user.getRoles().add(defaultRole);

// Automatically make your account an admin
        if ("ogboholisa@gmail.com".equalsIgnoreCase(email)) {

            Role adminRole = roleRepository.findByName(SystemRole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

            user.getRoles().add(adminRole);
        }

        User saved = userRepository.saveAndFlush(user);

        System.out.println("Created user ID = " + saved.getId());

        return saved;
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