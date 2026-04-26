package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.Faq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    Page<Faq> findByLanguageIgnoreCaseAndPageKeyIgnoreCaseAndStatusIgnoreCaseOrderByDisplayOrderAscIdAsc(
            String language,
            String pageKey,
            String status,
            Pageable pageable
    );
}