package com.inkFront.inFront.service.contact.impl;

import com.inkFront.inFront.entity.ContactMessage;
import com.inkFront.inFront.service.contact.ContactNotificationService;
import com.inkFront.inFront.service.email.BrevoEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactNotificationServiceImpl implements ContactNotificationService {

    private final BrevoEmailService brevoEmailService;
    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${inkfront.notifications.admin-email}")
    private String adminEmail;

    @Value("${app.mail.from}")
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

        if (message == null) {
            log.warn("Contact notification skipped: message is null.");
            return;
        }

        sendEmail(message);
        sendWhatsApp(message);
    }

    private void sendEmail(ContactMessage message) {
        log.info("========== USING BREVO EMAIL SERVICE ==========");

        try {

            brevoEmailService.sendTextEmail(
                    adminEmail,
                    "New InkFront Contact Message: " + safe(message.getSubject()),
                    buildMessageText(message),
                    safeEmail(message.getEmail())
            );

            log.info("Admin notification sent.");

        } catch (Exception ex) {

            log.error("Failed to send admin notification", ex);

        }

    }

    private void sendWhatsApp(ContactMessage message) {

        try {

            if (!whatsappEnabled) {
                return;
            }

            if (isBlank(whatsappPhoneNumberId)
                    || isBlank(whatsappAccessToken)
                    || isBlank(whatsappAdminPhone)) {

                log.warn("WhatsApp notification skipped.");
                return;
            }

            RestTemplate restTemplate = restTemplateBuilder.build();

            String url =
                    "https://graph.facebook.com/v20.0/"
                            + whatsappPhoneNumberId
                            + "/messages";

            Map<String, Object> body = Map.of(
                    "messaging_product", "whatsapp",
                    "to", whatsappAdminPhone,
                    "type", "text",
                    "text", Map.of(
                            "preview_url", false,
                            "body", buildWhatsAppText(message)
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(whatsappAccessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            restTemplate.postForEntity(
                    url,
                    request,
                    String.class
            );

        } catch (Exception ex) {

            log.error("Failed to send WhatsApp notification", ex);

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
                Priority: %s
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
                safe(message.getPriority()),
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

    private String safeEmail(String value) {

        return isBlank(value)
                ? senderEmail
                : value.trim();

    }

    private boolean isBlank(String value) {

        return value == null || value.trim().isEmpty();

    }

    private String safe(String value) {

        return isBlank(value)
                ? "N/A"
                : value.trim();

    }

}