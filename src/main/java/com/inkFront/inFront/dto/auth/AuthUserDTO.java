package com.inkFront.inFront.dto.auth;

import java.util.LinkedHashSet;
import java.util.Set;

public class AuthUserDTO {

    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String displayName;
    private String avatarUrl;
    private String provider;
    private boolean enabled;
    private Set<String> roles = new LinkedHashSet<>();

    public AuthUserDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getRoles() {
        if (roles == null) {
            roles = new LinkedHashSet<>();
        }
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles == null ? new LinkedHashSet<>() : roles;
    }
}