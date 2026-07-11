package com.inkFront.inFront.security;

import com.inkFront.inFront.entity.User;
import com.inkFront.inFront.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtCookieService jwtCookieService;

    @Value("${app.frontend.base-url:http://localhost:5173}")
    private String frontendBaseUrl;

    @Value("${app.frontend.oauth2-success-path:/login/success}")
    private String oauth2SuccessPath;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        System.out.println("========== SUCCESS HANDLER ==========");
        System.out.println("Authentication Class = " + authentication.getClass().getName());
        System.out.println("Principal Class = " + authentication.getPrincipal().getClass().getName());
        System.out.println("Authorities = " + authentication.getAuthorities());

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof OAuth2User oauth2User)) {
            throw new IllegalStateException(
                    "Expected OAuth2User but got " + principal.getClass().getName()
            );
        }

        System.out.println("OAuth Attributes = " + oauth2User.getAttributes());

        String email = oauth2User.getAttribute("email");

        System.out.println("OAuth Success Email = " + email);

        User user = userRepository.findByEmailIgnoreCase(email).orElse(null);

        System.out.println("User Found = " + (user != null));

        if (user == null) {
            throw new IllegalStateException(
                    "Provisioned OAuth user not found for email: " + email
            );
        }

        jwtCookieService.writeLoginCookies(request, response, user);
        System.out.println("FRONTEND URL FROM SPRING = " + frontendBaseUrl);
        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendBaseUrl)
                .path(oauth2SuccessPath)
                .queryParam("provider", "google")
                .build()
                .toUriString();

        System.out.println("Redirecting to = " + redirectUrl);

        response.sendRedirect(redirectUrl);
    }
}