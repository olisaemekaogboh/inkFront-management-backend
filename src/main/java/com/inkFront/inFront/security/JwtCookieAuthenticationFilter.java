package com.inkFront.inFront.security;

import com.inkFront.inFront.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtCookieAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtCookieService jwtCookieService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtCookieAuthenticationFilter(
            JwtService jwtService,
            JwtCookieService jwtCookieService,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.jwtService = jwtService;
        this.jwtCookieService = jwtCookieService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = jwtCookieService.extractAccessToken(request);

            if (
                    token != null &&
                            !token.isBlank() &&
                            jwtService.isTokenValid(token)
            ) {
                String email = jwtService.extractUsername(token);

                UserPrincipal userDetails =
                        customUserDetailsService.loadUserByUsername(email);

                System.out.println("JWT AUTH USER = " + email);
                System.out.println("JWT AUTHORITIES = " + userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}