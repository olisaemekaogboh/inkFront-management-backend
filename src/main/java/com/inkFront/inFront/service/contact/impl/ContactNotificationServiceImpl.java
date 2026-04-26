package com.inkFront.inFront.service.contact.impl;

import com.inkFront.inFront.entity.ContactMessage;
import com.inkFront.inFront.service.contact.ContactNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactNotificationServiceImpl implements ContactNotificationService {

    private final JavaMailSender mailSender;
    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${inkfront.notifications.admin-email:}")
    private String adminEmail;

    @Value("${spring.mail.username:}")
    private String senderEmail;

    @Value("${inkfront.notifications.whatsapp.enabled:false}")
    private boolean whatsappEnabled;

    @Value("${inkfront.notifications.whatsapp.phone-number-id:}")
    private String whatsappPhoneNumberId;

    @Value("${inkfront.notifications.whatsapp.access-token:}")
    private String whatsappAccessToken;

    @Value("${inkfront.notifications.whatsapp.admin-phone:}")
    private String whatsappAdminPhone;

    @Override
    public void notifyAdmin(ContactMessage message) {
        sendEmail(message);
        sendWhatsApp(message);
    }

    private void sendEmail(ContactMessage message) {
        try {
            if (isBlank(adminEmail) || isBlank(senderEmail)) {
                log.warn("Email notification skipped: admin email or sender email not configured.");
                return;
            }

            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(senderEmail);
            mail.setTo(adminEmail);
            mail.setSubject("New InkFront Contact Message: " + safe(message.getSubject()));
            mail.setText(buildMessageText(message));

            mailSender.send(mail);
        } catch (Exception ex) {
            log.error("Failed to send contact email notification", ex);
        }
    }

    private void sendWhatsApp(ContactMessage message) {
        try {
            if (!whatsappEnabled) {
                return;
            }

            if (isBlank(whatsappPhoneNumberId) || isBlank(whatsappAccessToken) || isBlank(whatsappAdminPhone)) {
                log.warn("WhatsApp notification skipped: WhatsApp credentials not configured.");
                return;
            }

            String url = "https://graph.facebook.com/v20.0/"
                    + whatsappPhoneNumberId
                    + "/messages";

            RestTemplate restTemplate = restTemplateBuilder.build();

            Map<String, Object> body = Map.of(
                    "messaging_product", "whatsapp",
                    "to", whatsappAdminPhone,
                    "type", "text",
                    "text", Map.of(
                            "preview_url", false,
                            "body", buildWhatsAppText(message)
                    )
            );

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setBearerAuth(whatsappAccessToken);
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(org.springframework.http.MediaType.APPLICATION_JSON));

            org.springframework.http.HttpEntity<Map<String, Object>> request =
                    new org.springframework.http.HttpEntity<>(body, headers);

            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception ex) {
            log.error("Failed to send WhatsApp contact notification", ex);
        }
    }

    private String buildMessageText(ContactMessage message) {
        return """
                New contact message received.

                Name: %s
                Email: %s
                Phone: %s
                Company: %s
                Service Interest: %s
                Preferred Language: %s
                Subject: %s

                Message:
                %s

                Status: %s
                Source: %s
                """.formatted(
                safe(message.getFullName()),
                safe(message.getEmail()),
                safe(message.getPhone()),
                safe(message.getCompany()),
                safe(message.getServiceInterest()),
                safe(message.getPreferredLanguage()),
                safe(message.getSubject()),
                safe(message.getMessage()),
                safe(message.getStatus()),
                safe(message.getSource())
        );
    }

    private String buildWhatsAppText(ContactMessage message) {
        return """
                New InkFront lead 🚀

                Name: %s
                Email: %s
                Phone: %s
                Service: %s
                Subject: %s

                %s
                """.formatted(
                safe(message.getFullName()),
                safe(message.getEmail()),
                safe(message.getPhone()),
                safe(message.getServiceInterest()),
                safe(message.getSubject()),
                safe(message.getMessage())
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String safe(String value) {
        return value == null || value.trim().isEmpty() ? "N/A" : value.trim();
    }
}