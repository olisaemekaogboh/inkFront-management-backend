package com.inkFront.inFront.dto.auth;

public class AuthResponseDTO {

    private AuthUserDTO user;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(AuthUserDTO user) {
        this.user = user;
    }

    public AuthUserDTO getUser() {
        return user;
    }

    public void setUser(AuthUserDTO user) {
        this.user = user;
    }
}