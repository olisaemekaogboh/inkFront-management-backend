package com.inkFront.inFront.service.contact.impl;

import com.inkFront.inFront.entity.ContactMessage;
import com.inkFront.inFront.service.contact.ContactNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactNotificationServiceImpl implements ContactNotificationService {

    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${inkfront.notifications.admin-email}")
    private String adminEmail;

    @Value("${app.mail.from}")
    private String senderEmail;

    @Value("${inkfront.notifications.brevo.api-key}")
    private String brevoApiKey;

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

        try {

            log.info("Sending contact notification through Brevo.");

            if (isBlank(adminEmail)
                    || isBlank(senderEmail)
                    || isBlank(brevoApiKey)) {

                log.warn("Brevo email skipped. Missing configuration.");
                return;
            }

            RestTemplate restTemplate = restTemplateBuilder.build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", brevoApiKey);

            Map<String, Object> body = Map.of(

                    "sender",
                    Map.of(
                            "name", "InkFront",
                            "email", senderEmail
                    ),

                    "to",
                    List.of(
                            Map.of(
                                    "email", adminEmail
                            )
                    ),

                    "replyTo",
                    Map.of(
                            "email", safeEmail(message.getEmail())
                    ),

                    "subject",
                    "New InkFront Contact Message: " + safe(message.getSubject()),

                    "textContent",
                    buildMessageText(message)
            );

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            "https://api.brevo.com/v3/smtp/email",
                            request,
                            String.class
                    );

            log.info("Brevo email sent successfully. Status={}", response.getStatusCode());

        } catch (Exception ex) {

            log.error("Failed to send Brevo email", ex);

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

                log.warn("WhatsApp notification skipped: credentials missing.");
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

            restTemplate.postForEntity(url, request, String.class);

            log.info("WhatsApp notification sent.");

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

        if (isBlank(value)) {
            return senderEmail;
        }

        return value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String safe(String value) {
        return isBlank(value) ? "N/A" : value.trim();
    }
}