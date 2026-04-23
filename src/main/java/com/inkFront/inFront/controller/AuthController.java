package com.inkFront.inFront.controller;



import com.inkFront.inFront.dto.api.ApiResponse;
import com.inkFront.inFront.dto.auth.AuthResponseDTO;
import com.inkFront.inFront.dto.auth.AuthUserDTO;
import com.inkFront.inFront.dto.auth.CsrfTokenDTO;
import com.inkFront.inFront.dto.auth.LoginRequestDTO;
import com.inkFront.inFront.dto.auth.RegisterRequestDTO;
import com.inkFront.inFront.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/csrf")
    public ResponseEntity<ApiResponse<CsrfTokenDTO>> csrf(@RequestAttribute(name = "_csrf") CsrfToken csrfToken) {
        CsrfTokenDTO dto = new CsrfTokenDTO(
                csrfToken.getToken(),
                csrfToken.getHeaderName(),
                csrfToken.getParameterName()
        );
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(
            @Valid @RequestBody RegisterRequestDTO requestDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        AuthResponseDTO authResponse = authService.register(requestDTO, request, response);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registration successful", authResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO requestDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        AuthResponseDTO authResponse = authService.login(requestDTO, request, response);
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        AuthResponseDTO authResponse = authService.refresh(request, response);
        return ResponseEntity.ok(ApiResponse.success("Session refreshed successfully", authResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.logout(request, response);
        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthUserDTO>> me() {
        return ResponseEntity.ok(ApiResponse.success(authService.getCurrentUser()));
    }
}