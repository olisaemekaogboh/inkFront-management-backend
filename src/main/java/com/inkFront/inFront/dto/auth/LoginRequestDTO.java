package com.inkFront.inFront.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {

    private String email;

    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    public LoginRequestDTO() {
    }

    public String getEmail() {
        return email != null && !email.isBlank() ? email : username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username != null && !username.isBlank() ? username : email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdentifier() {
        return getEmail();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}