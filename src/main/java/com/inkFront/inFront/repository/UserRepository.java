package com.inkFront.inFront.repository;



import com.inkFront.inFront.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByUsernameIgnoreCase(String username);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findById(Long id);

    boolean existsByEmailIgnoreCase(String email);
    Optional<User> findByProviderAndProviderUserId(User.AuthProvider provider, String providerUserId);
    boolean existsByUsernameIgnoreCase(String username);
}
