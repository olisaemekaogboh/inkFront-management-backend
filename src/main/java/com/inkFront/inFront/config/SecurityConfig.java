package com.inkFront.inFront.config;

import com.inkFront.inFront.security.CustomOAuth2UserService;
import com.inkFront.inFront.security.JwtCookieAuthenticationFilter;
import com.inkFront.inFront.security.JwtCookieProperties;
import com.inkFront.inFront.security.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableMethodSecurity
@EnableConfigurationProperties(JwtCookieProperties.class)
public class SecurityConfig {

    private final JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler successHandler;

    public SecurityConfig(
            JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter,
            CustomOAuth2UserService customOAuth2UserService,
            OAuth2AuthenticationSuccessHandler successHandler
    ) {
        this.jwtCookieAuthenticationFilter = jwtCookieAuthenticationFilter;
        this.customOAuth2UserService = customOAuth2UserService;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ObjectProvider<ClientRegistrationRepository> provider
    ) throws Exception {

        boolean oauthEnabled = provider.getIfAvailable() != null;

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/refresh",
                                "/api/auth/logout",
                                "/api/admin/**",
                                "/oauth2/**",
                                "/login/oauth2/**"
                        )
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/error",
                                "/favicon.ico",
                                "/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers(
                                "/api/public/**",
                                "/api/auth/**",
                                "/api/csrf",
                                "/oauth2/**",
                                "/login/oauth2/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/uploads/**", "/assets/**").permitAll()

                        .requestMatchers("/api/admin/**").hasAnyAuthority(
                                "ROLE_ADMIN",
                                "ROLE_SUPER_ADMIN"
                        )

                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtCookieAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .logout(logout -> logout.disable());

        if (oauthEnabled) {
            http.oauth2Login(oauth -> oauth
                    .userInfoEndpoint(user -> user.userService(customOAuth2UserService))
                    .successHandler(successHandler)
            );
        }

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }
}