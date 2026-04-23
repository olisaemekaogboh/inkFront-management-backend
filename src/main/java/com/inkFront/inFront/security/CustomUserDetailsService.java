package com.inkFront.inFront.security;

import com.inkFront.inFront.entity.User;
import com.inkFront.inFront.exception.ResourceNotFoundException;
import com.inkFront.inFront.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        User user = login.contains("@")
                ? userRepository.findByEmailIgnoreCase(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                : userRepository.findByUsernameIgnoreCase(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserPrincipal(user);
    }
}