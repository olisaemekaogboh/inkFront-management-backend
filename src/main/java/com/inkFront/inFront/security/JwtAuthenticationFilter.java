package com.inkFront.inFront.security;

import com.inkFront.inFront.config.SecurityCookieProperties;
import com.inkFront.inFront.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final CookieService cookieService;
    private final SecurityCookieProperties cookieProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/refresh",
            "/api/auth/logout",
            "/api/auth/csrf",
            "/api/public/**",
            "/api/contact/**",
            "/api/newsletter/**",
            "/api/health",
            "/api-docs/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/uploads/**",
            "/error",
            "/favicon.ico",
            "/oauth2/**",
            "/login/oauth2/**"
    );

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService,
            CookieService cookieService,
            SecurityCookieProperties cookieProperties
    ) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.cookieService = cookieService;
        this.cookieProperties = cookieProperties;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();

        boolean isPublic = PUBLIC_PATHS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));

        if (isPublic) {
            log.debug("Skipping JWT filter for public path: {}", path);
        }

        return isPublic;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String token = cookieService.getCookieValue(
                        request,
                        cookieProperties.getAccessTokenName()
                );

                if (token != null && !token.isBlank()) {
                    Claims claims = jwtService.extractAccessClaims(token);

                    String email = claims.getSubject();
                    if (email == null || email.isBlank()) {
                        email = claims.get("email", String.class);
                    }

                    if (email != null && !email.isBlank()) {
                        UserPrincipal principal =
                                (UserPrincipal) customUserDetailsService.loadUserByUsername(email);

                        if (principal != null && principal.isEnabled()) {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            principal,
                                            null,
                                            principal.getAuthorities()
                                    );

                            authentication.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)
                            );

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            log.debug("Successfully authenticated user: {}", email);
                        }
                    }
                }
            }
        } catch (JwtException ex) {
            log.debug("Invalid JWT token: {}", ex.getMessage());
            SecurityContextHolder.clearContext();
            cookieService.clearCookie(response, cookieProperties.getAccessTokenName());
        } catch (Exception ex) {
            log.error("Error processing JWT authentication", ex);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}