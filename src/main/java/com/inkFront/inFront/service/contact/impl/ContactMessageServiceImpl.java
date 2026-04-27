package com.inkFront.inFront.service.contact.impl;

import com.inkFront.inFront.dto.contact.ContactMessageReplyDTO;
import com.inkFront.inFront.dto.contact.ContactMessageRequestDTO;
import com.inkFront.inFront.dto.contact.ContactMessageResponseDTO;
import com.inkFront.inFront.dto.contact.ContactMessageStatsDTO;
import com.inkFront.inFront.dto.contact.ContactMessageUpdateDTO;
import com.inkFront.inFront.entity.ContactMessage;
import com.inkFront.inFront.repository.ContactMessageRepository;
import com.inkFront.inFront.service.contact.ContactMessageService;
import com.inkFront.inFront.service.contact.ContactNotificationService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ContactMessageServiceImpl implements ContactMessageService {

    private static final String DEFAULT_LANGUAGE = "EN";
    private static final String DEFAULT_STATUS = "NEW";
    private static final String DEFAULT_PRIORITY = "NORMAL";

    private static final Set<String> VALID_STATUSES = Set.of(
            "NEW",
            "CONTACTED",
            "IN_PROGRESS",
            "RESOLVED",
            "ARCHIVED"
    );

    private static final Set<String> VALID_PRIORITIES = Set.of(
            "LOW",
            "NORMAL",
            "HIGH",
            "URGENT"
    );

    private final ContactMessageRepository contactMessageRepository;
    private final ContactNotificationService contactNotificationService;
    private final JavaMailSender javaMailSender;

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
            log.error("Contact message saved, but admin notification failed for message id {}", saved.getId(), ex);
        }

        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactMessageResponseDTO> getAll(String status, String search, Pageable pageable) {
        boolean hasStatus = status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status);
        boolean hasSearch = search != null && !search.isBlank();

        if (hasStatus && hasSearch) {
            String normalizedStatus = normalizeStatus(status);
            String normalizedSearch = search.trim();

            return contactMessageRepository
                    .findByStatusIgnoreCaseAndFullNameContainingIgnoreCaseOrStatusIgnoreCaseAndEmailContainingIgnoreCaseOrderByCreatedAtDesc(
                            normalizedStatus,
                            normalizedSearch,
                            normalizedStatus,
                            normalizedSearch,
                            pageable
                    )
                    .map(this::toDto);
        }

        if (hasStatus) {
            return contactMessageRepository
                    .findByStatusIgnoreCaseOrderByCreatedAtDesc(normalizeStatus(status), pageable)
                    .map(this::toDto);
        }

        if (hasSearch) {
            String normalizedSearch = search.trim();

            return contactMessageRepository
                    .findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByCreatedAtDesc(
                            normalizedSearch,
                            normalizedSearch,
                            pageable
                    )
                    .map(this::toDto);
        }

        return contactMessageRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactMessageResponseDTO getById(Long id) {
        return toDto(findMessage(id));
    }

    @Override
    public ContactMessageResponseDTO update(Long id, ContactMessageUpdateDTO request) {
        ContactMessage message = findMessage(id);

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            message.setStatus(normalizeStatus(request.getStatus()));
        }

        if (request.getPriority() != null && !request.getPriority().isBlank()) {
            message.setPriority(normalizePriority(request.getPriority()));
        }

        if (request.getAdminNote() != null) {
            message.setAdminNote(normalizeNullable(request.getAdminNote()));
        }

        if (request.getAssignedTo() != null) {
            message.setAssignedTo(normalizeNullable(request.getAssignedTo()));
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

        if (!"RESOLVED".equalsIgnoreCase(message.getStatus())) {
            message.setResolvedAt(null);
        }

        return toDto(contactMessageRepository.save(message));
    }

    @Override
    public ContactMessageResponseDTO replyToMessage(Long id, ContactMessageReplyDTO request) {
        ContactMessage message = findMessage(id);

        String subject = normalize(request.getSubject(), "Re: " + message.getSubject());
        String body = normalize(request.getMessage(), "");
        String replyTo = normalizeNullable(request.getReplyTo());

        sendCustomerReply(message, subject, body, replyTo);

        if (request.getAdminNote() != null) {
            message.setAdminNote(normalizeNullable(request.getAdminNote()));
        }

        if (request.getAssignedTo() != null) {
            message.setAssignedTo(normalizeNullable(request.getAssignedTo()));
        }

        message.setStatus("CONTACTED");
        message.setLastContactedAt(LocalDateTime.now());

        return toDto(contactMessageRepository.save(message));
    }

    @Override
    @Transactional(readOnly = true)
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

    private void sendCustomerReply(ContactMessage contactMessage, String subject, String replyBody, String replyTo) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(contactMessage.getEmail());
            helper.setSubject(subject);

            if (replyTo != null) {
                helper.setReplyTo(replyTo);
            }

            helper.setText(buildReplyHtml(contactMessage, replyBody), true);

            javaMailSender.send(mimeMessage);
        } catch (Exception ex) {
            log.error("Failed to send contact reply email for message id {}", contactMessage.getId(), ex);
            throw new RuntimeException("Reply email could not be sent. Please check mail settings and try again.");
        }
    }

    private String buildReplyHtml(ContactMessage contactMessage, String replyBody) {
        return """
                <!doctype html>
                <html>
                <body style="margin:0;padding:0;background:#f6f7fb;font-family:Arial,sans-serif;color:#111827;">
                  <div style="max-width:680px;margin:0 auto;padding:32px 16px;">
                    <div style="background:#ffffff;border-radius:18px;padding:28px;border:1px solid #e5e7eb;">
                      <h2 style="margin:0 0 16px;color:#111827;">Hello %s,</h2>
                      <div style="font-size:15px;line-height:1.7;color:#374151;">
                        %s
                      </div>
                      <hr style="border:none;border-top:1px solid #e5e7eb;margin:28px 0;" />
                      <p style="font-size:13px;color:#6b7280;margin:0;">
                        This reply is about your message: <strong>%s</strong>
                      </p>
                    </div>
                  </div>
                </body>
                </html>
                """.formatted(
                escapeHtml(contactMessage.getFullName()),
                nl2br(escapeHtml(replyBody)),
                escapeHtml(contactMessage.getSubject())
        );
    }

    private ContactMessage findMessage(Long id) {
        return contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact message not found with id: " + id));
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

    private String normalizeStatus(String status) {
        String normalized = normalize(status, DEFAULT_STATUS).toUpperCase();

        if (!VALID_STATUSES.contains(normalized)) {
            throw new RuntimeException("Invalid contact message status: " + status);
        }

        return normalized;
    }

    private String normalizePriority(String priority) {
        String normalized = normalize(priority, DEFAULT_PRIORITY).toUpperCase();

        if (!VALID_PRIORITIES.contains(normalized)) {
            throw new RuntimeException("Invalid contact message priority: " + priority);
        }

        return normalized;
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

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String nl2br(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("\n", "<br />");
    }
}