package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    Page<ContactMessage> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ContactMessage> findByStatusIgnoreCaseOrderByCreatedAtDesc(String status, Pageable pageable);

    long countByStatusIgnoreCase(String status);
}