package com.inkFront.inFront.controller;

import com.inkFront.inFront.dto.auth.AuthResponseDTO;
import com.inkFront.inFront.dto.auth.AuthUserDTO;
import com.inkFront.inFront.dto.auth.LoginRequestDTO;
import com.inkFront.inFront.dto.auth.RegisterRequestDTO;
import com.inkFront.inFront.entity.User;
import com.inkFront.inFront.repository.UserRepository;
import com.inkFront.inFront.security.JwtCookieService;
import com.inkFront.inFront.security.JwtService;
import com.inkFront.inFront.security.UserPrincipal;
import com.inkFront.inFront.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtCookieService jwtCookieService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(
            AuthService authService,
            JwtCookieService jwtCookieService,
            JwtService jwtService,
            UserRepository userRepository
    ) {
        this.authService = authService;
        this.jwtCookieService = jwtCookieService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO requestDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.register(requestDTO, request, response));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO requestDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.login(requestDTO, request, response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authService.refresh(request, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.logout(request, response);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("message", "Logout successful");

        return ResponseEntity.ok(body);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(HttpServletRequest request) {
        User user = resolveUserFromSecurityContext();

        if (user == null) {
            user = resolveUserFromAccessCookie(request);
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", true);
        body.put("authenticated", user != null);
        body.put("data", user == null ? null : toAuthUser(user));

        return ResponseEntity.ok(body);
    }

    private User resolveUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getUser();
        }

        if (authentication.getName() == null || "anonymousUser".equals(authentication.getName())) {
            return null;
        }

        return userRepository.findByEmailIgnoreCase(authentication.getName()).orElse(null);
    }

    private User resolveUserFromAccessCookie(HttpServletRequest request) {
        try {
            String token = jwtCookieService.extractAccessToken(request);

            if (token == null || token.isBlank()) {
                return null;
            }

            if (!jwtService.isTokenValid(token)) {
                return null;
            }

            String email = jwtService.extractUsername(token);

            return userRepository.findByEmailIgnoreCase(email).orElse(null);
        } catch (Exception ignored) {
            return null;
        }
    }

    private AuthUserDTO toAuthUser(User user) {
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
        dto.setRoles(
                user.getRoles()
                        .stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toCollection(java.util.LinkedHashSet::new))
        );

        return dto;
    }
}