package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.NewsletterSubscriber;
import com.inkFront.inFront.entity.enums.NewsletterSubscriberStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsletterSubscriberRepository extends JpaRepository<NewsletterSubscriber, Long> {

    Optional<NewsletterSubscriber> findByEmailIgnoreCase(String email);

    Optional<NewsletterSubscriber> findByUnsubscribeToken(String unsubscribeToken);

    boolean existsByEmailIgnoreCase(String email);

    Page<NewsletterSubscriber> findAllByStatus(
            NewsletterSubscriberStatus status,
            Pageable pageable
    );

    Page<NewsletterSubscriber> findAllByLanguage(
            SupportedLanguage language,
            Pageable pageable
    );

    Page<NewsletterSubscriber> findAllByLanguageAndStatus(
            SupportedLanguage language,
            NewsletterSubscriberStatus status,
            Pageable pageable
    );

    Page<NewsletterSubscriber> findAllByEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(
            String email,
            String fullName,
            Pageable pageable
    );

    List<NewsletterSubscriber> findAllByStatusAndLanguage(
            NewsletterSubscriberStatus status,
            SupportedLanguage language
    );

    List<NewsletterSubscriber> findAllByStatus(
            NewsletterSubscriberStatus status
    );
}