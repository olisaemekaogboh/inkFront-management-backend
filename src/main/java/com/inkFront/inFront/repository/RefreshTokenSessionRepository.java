package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.RefreshTokenSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenSessionRepository extends JpaRepository<RefreshTokenSession, Long> {

    Optional<RefreshTokenSession> findByTokenId(String tokenId);

    @Query("""
            select r
            from RefreshTokenSession r
            join fetch r.user
            where r.revoked = false
              and r.expiresAt > :now
            order by r.createdAt desc
            """)
    List<RefreshTokenSession> findAllActiveCandidates(Instant now);
}