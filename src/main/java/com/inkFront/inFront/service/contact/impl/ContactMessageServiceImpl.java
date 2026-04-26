package com.inkFront.inFront.service.contact.impl;

import com.inkFront.inFront.dto.contact.ContactMessageRequestDTO;
import com.inkFront.inFront.dto.contact.ContactMessageResponseDTO;
import com.inkFront.inFront.entity.ContactMessage;
import com.inkFront.inFront.repository.ContactMessageRepository;
import com.inkFront.inFront.service.contact.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactMessageServiceImpl implements ContactMessageService {

    private static final String DEFAULT_LANGUAGE = "EN";
    private static final String DEFAULT_STATUS = "NEW";

    private final ContactMessageRepository contactMessageRepository;

    @Override
    public ContactMessageResponseDTO submit(ContactMessageRequestDTO request) {
        ContactMessage message = ContactMessage.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .company(request.getCompany())
                .serviceInterest(request.getServiceInterest())
                .preferredLanguage(normalize(request.getPreferredLanguage(), DEFAULT_LANGUAGE))
                .subject(request.getSubject())
                .message(request.getMessage())
                .status(DEFAULT_STATUS)
                .build();

        return toDto(contactMessageRepository.save(message));
    }

    @Override
    public Page<ContactMessageResponseDTO> getAll(Pageable pageable) {
        return contactMessageRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::toDto);
    }

    @Override
    public ContactMessageResponseDTO updateStatus(Long id, String status, String adminNote) {
        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact message not found"));

        message.setStatus(normalize(status, message.getStatus()));
        message.setAdminNote(adminNote);

        return toDto(contactMessageRepository.save(message));
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
                .adminNote(message.getAdminNote())
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
}