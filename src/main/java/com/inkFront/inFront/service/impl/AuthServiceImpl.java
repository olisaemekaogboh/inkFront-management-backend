package com.inkFront.inFront.service.impl;

import com.inkFront.inFront.config.SecurityCookieProperties;
import com.inkFront.inFront.dto.auth.AuthResponseDTO;
import com.inkFront.inFront.dto.auth.AuthUserDTO;
import com.inkFront.inFront.dto.auth.LoginRequestDTO;
import com.inkFront.inFront.dto.auth.RegisterRequestDTO;
import com.inkFront.inFront.entity.RefreshTokenSession;
import com.inkFront.inFront.entity.Role;
import com.inkFront.inFront.entity.User;
import com.inkFront.inFront.entity.enums.SystemRole;
import com.inkFront.inFront.exception.DuplicateResourceException;
import com.inkFront.inFront.exception.InvalidRequestException;
import com.inkFront.inFront.exception.ResourceNotFoundException;
import com.inkFront.inFront.repository.RefreshTokenSessionRepository;
import com.inkFront.inFront.repository.RoleRepository;
import com.inkFront.inFront.repository.UserRepository;
import com.inkFront.inFront.security.CookieService;
import com.inkFront.inFront.security.JwtService;
import com.inkFront.inFront.security.UserPrincipal;
import com.inkFront.inFront.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenSessionRepository refreshTokenSessionRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final SecurityCookieProperties cookieProperties;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            RefreshTokenSessionRepository refreshTokenSessionRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            CookieService cookieService,
            SecurityCookieProperties cookieProperties
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenSessionRepository = refreshTokenSessionRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.cookieProperties = cookieProperties;
    }

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO requestDTO, HttpServletRequest request, HttpServletResponse response) {
        String email = normalizeEmail(requestDTO.getEmail());
        String username = normalizeUsername(requestDTO.getUsername());

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Email is already in use");
        }

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new DuplicateResourceException("Username is already in use");
        }

        Role defaultRole = roleRepository.findByName(SystemRole.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        User user = new User();
        user.setFirstName(requestDTO.getFirstName().trim());
        user.setLastName(requestDTO.getLastName().trim());
        user.setEmail(email);
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(requestDTO.getPassword()));
        user.setEnabled(true);
        user.setAccountNonLocked(true);

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        return issueFreshSession(savedUser, request, response);
    }

    @Override
    @Transactional
    public AuthResponseDTO login(LoginRequestDTO requestDTO, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDTO.getLogin().trim(), requestDTO.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return issueFreshSession(user, request, response);
    }

    @Override
    @Transactional
    public AuthResponseDTO refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieService.getCookieValue(request, cookieProperties.getRefreshTokenName());
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException("Refresh token is required");
        }

        try {
            Claims claims = jwtService.extractRefreshClaims(refreshToken);
            String tokenId = claims.getId();
            Long userId = Long.parseLong(claims.getSubject());

            RefreshTokenSession existingSession = refreshTokenSessionRepository.findByTokenId(tokenId)
                    .orElseThrow(() -> new BadCredentialsException("Refresh token session not found"));

            if (Boolean.TRUE.equals(existingSession.getRevoked())) {
                throw new BadCredentialsException("Refresh token has been revoked");
            }

            if (existingSession.getExpiresAt().isBefore(LocalDateTime.now())) {
                existingSession.setRevoked(true);
                refreshTokenSessionRepository.save(existingSession);
                throw new BadCredentialsException("Refresh token has expired");
            }

            String incomingHash = sha256(refreshToken);
            if (!incomingHash.equals(existingSession.getTokenHash())) {
                existingSession.setRevoked(true);
                refreshTokenSessionRepository.save(existingSession);
                throw new BadCredentialsException("Refresh token mismatch");
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            rotateRefreshSession(existingSession, user, request, response);
            return new AuthResponseDTO(toAuthUserDTO(user));
        } catch (JwtException ex) {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = cookieService.getCookieValue(request, cookieProperties.getRefreshTokenName());

        if (refreshToken != null && !refreshToken.isBlank()) {
            try {
                Claims claims = jwtService.extractRefreshClaims(refreshToken);
                refreshTokenSessionRepository.findByTokenId(claims.getId()).ifPresent(session -> {
                    session.setRevoked(true);
                    refreshTokenSessionRepository.save(session);
                });
            } catch (JwtException ignored) {
            }
        }

        response.addHeader(HttpHeaders.SET_COOKIE, cookieService.clearAccessTokenCookie());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieService.clearRefreshTokenCookie());
        SecurityContextHolder.clearContext();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthUserDTO getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserPrincipal userPrincipal)) {
            throw new BadCredentialsException("User is not authenticated");
        }

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return toAuthUserDTO(user);
    }

    private AuthResponseDTO issueFreshSession(User user, HttpServletRequest request, HttpServletResponse response) {
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new DisabledException("User account is disabled");
        }

        UserPrincipal principal = new UserPrincipal(user);
        String accessToken = jwtService.generateAccessToken(principal);
        String refreshTokenId = jwtService.generateTokenId();
        String refreshToken = jwtService.generateRefreshToken(user.getId(), refreshTokenId);

        RefreshTokenSession refreshSession = new RefreshTokenSession();
        refreshSession.setTokenId(refreshTokenId);
        refreshSession.setTokenHash(sha256(refreshToken));
        refreshSession.setUser(user);
        refreshSession.setExpiresAt(jwtService.extractRefreshExpiry(refreshToken));
        refreshSession.setRevoked(false);
        refreshSession.setUserAgent(truncate(request.getHeader("User-Agent"), 255));
        refreshSession.setIpAddress(truncate(resolveClientIp(request), 64));
        refreshTokenSessionRepository.save(refreshSession);

        response.addHeader(HttpHeaders.SET_COOKIE, cookieService.buildAccessTokenCookie(accessToken));
        response.addHeader(HttpHeaders.SET_COOKIE, cookieService.buildRefreshTokenCookie(refreshToken));

        return new AuthResponseDTO(toAuthUserDTO(user));
    }

    private void rotateRefreshSession(
            RefreshTokenSession existingSession,
            User user,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        existingSession.setRevoked(true);

        UserPrincipal principal = new UserPrincipal(user);
        String accessToken = jwtService.generateAccessToken(principal);
        String newTokenId = jwtService.generateTokenId();
        String newRefreshToken = jwtService.generateRefreshToken(user.getId(), newTokenId);

        existingSession.setReplacedByTokenId(newTokenId);
        refreshTokenSessionRepository.save(existingSession);

        RefreshTokenSession newSession = new RefreshTokenSession();
        newSession.setTokenId(newTokenId);
        newSession.setTokenHash(sha256(newRefreshToken));
        newSession.setUser(user);
        newSession.setExpiresAt(jwtService.extractRefreshExpiry(newRefreshToken));
        newSession.setRevoked(false);
        newSession.setUserAgent(truncate(request.getHeader("User-Agent"), 255));
        newSession.setIpAddress(truncate(resolveClientIp(request), 64));
        refreshTokenSessionRepository.save(newSession);

        response.addHeader(HttpHeaders.SET_COOKIE, cookieService.buildAccessTokenCookie(accessToken));
        response.addHeader(HttpHeaders.SET_COOKIE, cookieService.buildRefreshTokenCookie(newRefreshToken));
    }

    private AuthUserDTO toAuthUserDTO(User user) {
        AuthUserDTO dto = new AuthUserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setFullName((user.getFirstName() + " " + user.getLastName()).trim());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setEnabled(user.getEnabled());
        dto.setRoles(
                user.getRoles()
                        .stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    private String normalizeEmail(String value) {
        return value.trim().toLowerCase();
    }

    private String normalizeUsername(String value) {
        return value.trim().toLowerCase();
    }

    private String resolveClientIp(HttpServletRequest request) {
        String header = request.getHeader("X-Forwarded-For");
        if (header != null && !header.isBlank()) {
            return header.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (Exception ex) {
            throw new InvalidRequestException("Unable to hash refresh token");
        }
    }
}