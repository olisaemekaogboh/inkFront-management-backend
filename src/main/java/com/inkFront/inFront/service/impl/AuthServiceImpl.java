package com.inkFront.inFront.service.impl;

import com.inkFront.inFront.dto.auth.AuthResponseDTO;
import com.inkFront.inFront.dto.auth.AuthUserDTO;
import com.inkFront.inFront.dto.auth.LoginRequestDTO;
import com.inkFront.inFront.dto.auth.RegisterRequestDTO;
import com.inkFront.inFront.entity.Role;
import com.inkFront.inFront.entity.User;
import com.inkFront.inFront.entity.enums.SystemRole;
import com.inkFront.inFront.repository.RoleRepository;
import com.inkFront.inFront.repository.UserRepository;
import com.inkFront.inFront.security.JwtCookieService;
import com.inkFront.inFront.security.RefreshTokenService;
import com.inkFront.inFront.security.UserPrincipal;
import com.inkFront.inFront.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCookieService jwtCookieService;
    private final RefreshTokenService refreshTokenService;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtCookieService jwtCookieService,
            RefreshTokenService refreshTokenService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtCookieService = jwtCookieService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public AuthResponseDTO register(
            RegisterRequestDTO requestDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String email = requestDTO.getEmail() == null
                ? null
                : requestDTO.getEmail().trim().toLowerCase();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (userRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role userRole = roleRepository.findByName(SystemRole.USER)
                .orElseGet(() -> roleRepository.save(new Role(SystemRole.USER)));

        User user = new User();
        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        user.setDisplayName(buildDisplayName(requestDTO.getFirstName(), requestDTO.getLastName(), email));
        user.setEmail(email);
        user.setUsername(email);
        user.setPasswordHash(passwordEncoder.encode(requestDTO.getPassword()));
        user.setProvider(User.AuthProvider.LOCAL);
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setEmailVerified(false);
        user.addRole(userRole);

        User savedUser = userRepository.save(user);
        jwtCookieService.writeLoginCookies(request, response, savedUser);

        return buildAuthResponse(savedUser, "Registration successful");
    }

    @Override
    public AuthResponseDTO login(
            LoginRequestDTO requestDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getIdentifier(),
                        requestDTO.getPassword()
                )
        );

        User user = resolveAuthenticatedUser(authentication);
        jwtCookieService.writeLoginCookies(request, response, user);

        return buildAuthResponse(user, "Login successful");
    }

    @Override
    public AuthResponseDTO refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String oldRefreshToken = jwtCookieService.extractRefreshToken(request);
        User user = refreshTokenService.validateAndResolveUser(oldRefreshToken);

        refreshTokenService.rotate(oldRefreshToken, user, request);
        jwtCookieService.clearAuthCookies(request, response);
        jwtCookieService.writeLoginCookies(request, response, user);

        return buildAuthResponse(user, "Session refreshed");
    }

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        jwtCookieService.clearAuthCookies(request, response);
        SecurityContextHolder.clearContext();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthUserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            return null;
        }

        User user = resolveAuthenticatedUser(authentication);
        return buildAuthUser(user);
    }

    private User resolveAuthenticatedUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getUser();
        }

        return userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }

    private AuthResponseDTO buildAuthResponse(User user, String message) {
        AuthResponseDTO response = new AuthResponseDTO();
        response.setSuccess(true);
        response.setMessage(message);
        response.setUser(buildAuthUser(user));
        return response;
    }

    private AuthUserDTO buildAuthUser(User user) {
        AuthUserDTO dto = new AuthUserDTO();

        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDisplayName(user.getDisplayName());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setProvider(user.getProvider() == null ? "LOCAL" : user.getProvider().name());
        dto.setEnabled(Boolean.TRUE.equals(user.getEnabled()));

        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        dto.setRoles(roles);

        return dto;
    }

    private String buildDisplayName(String firstName, String lastName, String fallbackEmail) {
        String first = firstName == null ? "" : firstName.trim();
        String last = lastName == null ? "" : lastName.trim();
        String fullName = (first + " " + last).trim();

        return fullName.isBlank() ? fallbackEmail : fullName;
    }
}