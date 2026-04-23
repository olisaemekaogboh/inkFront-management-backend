package com.inkFront.inFront.repository;



import com.inkFront.inFront.entity.RefreshTokenSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenSessionRepository extends JpaRepository<RefreshTokenSession, Long> {
    Optional<RefreshTokenSession> findByTokenId(String tokenId);
}