package com.inkFront.inFront.service;



import com.inkFront.inFront.dto.auth.AuthResponseDTO;
import com.inkFront.inFront.dto.auth.AuthUserDTO;
import com.inkFront.inFront.dto.auth.LoginRequestDTO;
import com.inkFront.inFront.dto.auth.RegisterRequestDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO requestDTO, HttpServletRequest request, HttpServletResponse response);
    AuthResponseDTO login(LoginRequestDTO requestDTO, HttpServletRequest request, HttpServletResponse response);
    AuthResponseDTO refresh(HttpServletRequest request, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
    AuthUserDTO getCurrentUser();
}