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



        Object principal = authentication.getPrincipal();

        if (!(principal instanceof OAuth2User oauth2User)) {
            throw new IllegalStateException(
                    "Expected OAuth2User but got " + principal.getClass().getName()
            );
        }



        String email = oauth2User.getAttribute("email");


        User user = userRepository.findByEmailIgnoreCase(email).orElse(null);



        if (user == null) {
            throw new IllegalStateException(
                    "Provisioned OAuth user not found for email: " + email
            );
        }

        jwtCookieService.writeLoginCookies(request, response, user);

        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendBaseUrl)
                .path(oauth2SuccessPath)
                .queryParam("provider", "google")
                .build()
                .toUriString();



        response.sendRedirect(redirectUrl);
    }
}