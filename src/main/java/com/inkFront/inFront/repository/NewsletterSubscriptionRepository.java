package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.NewsletterSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsletterSubscriptionRepository extends JpaRepository<NewsletterSubscription, Long> {
    Optional<NewsletterSubscription> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}