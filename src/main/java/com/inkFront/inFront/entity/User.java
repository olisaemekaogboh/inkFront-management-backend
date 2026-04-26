package com.inkFront.inFront.entity;

import com.inkFront.inFront.entity.base.AuditableEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_users_email", columnList = "email"),
                @Index(name = "idx_users_username", columnList = "username"),
                @Index(name = "idx_users_provider_provider_user_id", columnList = "provider, provider_user_id")
        }
)
public class User extends AuditableEntity {

    public enum AuthProvider {
        LOCAL,
        GOOGLE
    }

    @Column(name = "username", unique = true, length = 190)
    private String username;

    @Column(name = "first_name", length = 120)
    private String firstName;

    @Column(name = "last_name", length = 120)
    private String lastName;

    @Column(name = "display_name", length = 180)
    private String displayName;

    @Column(name = "email", nullable = false, unique = true, length = 190)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 30)
    private AuthProvider provider = AuthProvider.LOCAL;

    @Column(name = "provider_user_id", length = 190)
    private String providerUserId;

    @Column(name = "avatar_url", length = 700)
    private String avatarUrl;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public Long getId() {
        return super.getId();
    }

    public String getUsername() {
        return username != null && !username.isBlank() ? username : email;
    }

    public void setUsername(String username) {
        this.username = clean(username);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = clean(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = clean(lastName);
    }

    public String getDisplayName() {
        if (displayName != null && !displayName.isBlank()) {
            return displayName;
        }

        String fullName = ((firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName)).trim();
        return fullName.isBlank() ? email : fullName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = clean(displayName);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String cleaned = email == null ? null : email.trim().toLowerCase();
        this.email = cleaned;

        if (this.username == null || this.username.isBlank()) {
            this.username = cleaned;
        }
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPassword() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setPassword(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider == null ? AuthProvider.LOCAL : provider;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = clean(providerUserId);
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = clean(avatarUrl);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled != null && enabled;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked == null || accountNonLocked;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified != null && emailVerified;
    }

    public Set<Role> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles == null ? new HashSet<>() : roles;
    }

    public void addRole(Role role) {
        if (role != null) {
            getRoles().add(role);
        }
    }

    private String clean(String value) {
        return value == null ? null : value.trim();
    }
}