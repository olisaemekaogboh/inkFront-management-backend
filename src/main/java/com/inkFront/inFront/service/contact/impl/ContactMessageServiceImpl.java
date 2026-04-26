package com.inkFront.inFront.service.contact.impl;

import com.inkFront.inFront.dto.contact.ContactMessageRequestDTO;
import com.inkFront.inFront.dto.contact.ContactMessageResponseDTO;
import com.inkFront.inFront.dto.contact.ContactMessageStatsDTO;
import com.inkFront.inFront.dto.contact.ContactMessageUpdateDTO;
import com.inkFront.inFront.entity.ContactMessage;
import com.inkFront.inFront.repository.ContactMessageRepository;
import com.inkFront.inFront.service.contact.ContactMessageService;
import com.inkFront.inFront.service.contact.ContactNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactMessageServiceImpl implements ContactMessageService {

    private static final String DEFAULT_LANGUAGE = "EN";
    private static final String DEFAULT_STATUS = "NEW";
    private static final String DEFAULT_PRIORITY = "NORMAL";

    private final ContactMessageRepository contactMessageRepository;
    private final ContactNotificationService contactNotificationService;

    @Override
    public ContactMessageResponseDTO submit(ContactMessageRequestDTO request) {
        ContactMessage message = ContactMessage.builder()
                .fullName(normalize(request.getFullName(), "Unknown Client"))
                .email(normalize(request.getEmail(), ""))
                .phone(normalizeNullable(request.getPhone()))
                .company(normalizeNullable(request.getCompany()))
                .serviceInterest(normalizeNullable(request.getServiceInterest()))
                .preferredLanguage(normalize(request.getPreferredLanguage(), DEFAULT_LANGUAGE).toUpperCase())
                .subject(normalize(request.getSubject(), "Website inquiry"))
                .message(normalize(request.getMessage(), ""))
                .status(DEFAULT_STATUS)
                .priority(DEFAULT_PRIORITY)
                .source("WEBSITE")
                .build();

        ContactMessage saved = contactMessageRepository.save(message);

        try {
            contactNotificationService.notifyAdmin(saved);
        } catch (Exception ex) {
            log.error("Contact message saved, but notification failed for message id {}", saved.getId(), ex);
        }

        return toDto(saved);
    }

    @Override
    public Page<ContactMessageResponseDTO> getAll(String status, Pageable pageable) {
        if (status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status)) {
            return contactMessageRepository
                    .findByStatusIgnoreCaseOrderByCreatedAtDesc(status.trim(), pageable)
                    .map(this::toDto);
        }

        return contactMessageRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::toDto);
    }

    @Override
    public ContactMessageResponseDTO getById(Long id) {
        return toDto(findMessage(id));
    }

    @Override
    public ContactMessageResponseDTO update(Long id, ContactMessageUpdateDTO request) {
        ContactMessage message = findMessage(id);

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            message.setStatus(request.getStatus().trim().toUpperCase());
        }

        if (request.getPriority() != null && !request.getPriority().isBlank()) {
            message.setPriority(request.getPriority().trim().toUpperCase());
        }

        if (request.getAdminNote() != null) {
            message.setAdminNote(request.getAdminNote());
        }

        if (request.getAssignedTo() != null) {
            message.setAssignedTo(request.getAssignedTo());
        }

        if (Boolean.TRUE.equals(request.getMarkContacted())) {
            message.setLastContactedAt(LocalDateTime.now());

            if ("NEW".equalsIgnoreCase(message.getStatus())) {
                message.setStatus("CONTACTED");
            }
        }

        if ("RESOLVED".equalsIgnoreCase(message.getStatus()) && message.getResolvedAt() == null) {
            message.setResolvedAt(LocalDateTime.now());
        }

        return toDto(contactMessageRepository.save(message));
    }

    @Override
    public ContactMessageStatsDTO getStats() {
        return ContactMessageStatsDTO.builder()
                .total(contactMessageRepository.count())
                .newMessages(contactMessageRepository.countByStatusIgnoreCase("NEW"))
                .inProgress(contactMessageRepository.countByStatusIgnoreCase("IN_PROGRESS"))
                .contacted(contactMessageRepository.countByStatusIgnoreCase("CONTACTED"))
                .resolved(contactMessageRepository.countByStatusIgnoreCase("RESOLVED"))
                .archived(contactMessageRepository.countByStatusIgnoreCase("ARCHIVED"))
                .build();
    }

    @Override
    public void delete(Long id) {
        ContactMessage message = findMessage(id);
        contactMessageRepository.delete(message);
    }

    private ContactMessage findMessage(Long id) {
        return contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact message not found"));
    }

    private ContactMessageResponseDTO toDto(ContactMessage message) {
        return ContactMessageResponseDTO.builder()
                .id(message.getId())
                .fullName(message.getFullName())
                .email(message.getEmail())
                .phone(message.getPhone())
                .company(message.getCompany())
                .serviceInterest(message.getServiceInterest())
                .preferredLanguage(message.getPreferredLanguage())
                .subject(message.getSubject())
                .message(message.getMessage())
                .status(message.getStatus())
                .priority(message.getPriority())
                .adminNote(message.getAdminNote())
                .assignedTo(message.getAssignedTo())
                .source(message.getSource())
                .lastContactedAt(message.getLastContactedAt())
                .resolvedAt(message.getResolvedAt())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }

    private String normalize(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }

        return value.trim();
    }

    private String normalizeNullable(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
    }
}