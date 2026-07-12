package com.inkFront.inFront.service.email.impl;

import com.inkFront.inFront.service.email.BrevoEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrevoEmailServiceImpl implements BrevoEmailService {

    private static final String BREVO_URL =
            "https://api.brevo.com/v3/smtp/email";

    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${inkfront.notifications.brevo.api-key}")
    private String apiKey;

    @Value("${app.mail.from}")
    private String fromEmail;

    private RestTemplate restTemplate() {
        return restTemplateBuilder.build();
    }

    @Override
    public void sendTextEmail(
            String to,
            String subject,
            String textContent,
            String replyTo
    ) {
        send(
                to,
                subject,
                textContent,
                null,
                replyTo
        );
    }

    @Override
    public void sendHtmlEmail(
            String to,
            String subject,
            String htmlContent,
            String replyTo
    ) {
        send(
                to,
                subject,
                null,
                htmlContent,
                replyTo
        );
    }

    private void send(
            String to,
            String subject,
            String text,
            String html,
            String replyTo
    ) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Brevo API key is not configured.");
        }

        if (fromEmail == null || fromEmail.isBlank()) {
            throw new IllegalStateException("MAIL_FROM is not configured.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> body = new HashMap<>();

        body.put("sender", Map.of(
                "name", "InkFront",
                "email", fromEmail
        ));

        body.put("to", List.of(
                Map.of("email", to)
        ));

        body.put("subject", subject);

        if (replyTo != null && !replyTo.isBlank()) {
            body.put("replyTo", Map.of(
                    "email", replyTo
            ));
        }

        if (text != null && !text.isBlank()) {
            body.put("textContent", text);
        }

        if (html != null && !html.isBlank()) {
            body.put("htmlContent", html);
        }

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {

            restTemplate().postForEntity(
                    BREVO_URL,
                    request,
                    String.class
            );

            log.info("Brevo email sent successfully to {}", to);

        } catch (HttpStatusCodeException ex) {

            log.error(
                    "Brevo API Error {}: {}",
                    ex.getStatusCode(),
                    ex.getResponseBodyAsString()
            );

            throw new RuntimeException(
                    "Brevo API Error: " + ex.getResponseBodyAsString(),
                    ex
            );

        } catch (RestClientException ex) {

            log.error("Failed to connect to Brevo", ex);

            throw new RuntimeException(
                    "Unable to connect to Brevo.",
                    ex
            );

        }
    }
}