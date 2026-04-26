package com.inkFront.inFront.service;

import com.inkFront.inFront.entity.User;
import com.inkFront.inFront.repository.UserRepository;
import com.inkFront.inFront.security.UserPrincipal;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!Boolean.TRUE.equals(appUser.getEnabled())) {
            throw new DisabledException("User account is disabled");
        }

        if (!Boolean.TRUE.equals(appUser.getAccountNonLocked())) {
            throw new LockedException("User account is locked");
        }

        return UserPrincipal.create(appUser);
    }
}