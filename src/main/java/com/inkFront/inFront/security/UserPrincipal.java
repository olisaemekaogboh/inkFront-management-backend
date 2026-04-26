package com.inkFront.inFront.security;

import com.inkFront.inFront.entity.Role;
import com.inkFront.inFront.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private final User user;
    private final Set<GrantedAuthority> authorities;

    private UserPrincipal(User user) {
        this.user = user;
        this.authorities = buildAuthorities(user);
    }

    public static UserPrincipal create(User user) {
        return new UserPrincipal(user);
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash() == null ? "" : user.getPasswordHash();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    private Set<GrantedAuthority> buildAuthorities(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return user.getRoles()
                .stream()
                .map(Role::getName)
                .map(Enum::name)
                .filter(role -> role != null && !role.isBlank())
                .map(UserPrincipal::normalizeAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static String normalizeAuthority(String role) {
        String cleanRole = role.trim().toUpperCase();
        return cleanRole.startsWith("ROLE_") ? cleanRole : "ROLE_" + cleanRole;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE.equals(user.getAccountNonLocked());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getEnabled());
    }
}