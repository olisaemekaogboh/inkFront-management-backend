package com.inkFront.inFront.service.email.impl;

import com.inkFront.inFront.service.email.BrevoEmailService;
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

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> body = Map.of(
                "sender",
                Map.of(
                        "name", "InkFront",
                        "email", fromEmail
                ),
                "to",
                List.of(
                        Map.of(
                                "email", to
                        )
                ),
                "subject",
                subject,
                "replyTo",
                Map.of(
                        "email",
                        replyTo == null || replyTo.isBlank()
                                ? fromEmail
                                : replyTo
                ),
                "textContent",
                text == null ? "" : text,
                "htmlContent",
                html == null ? "" : html
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate().postForEntity(
                        BREVO_URL,
                        request,
                        String.class
                );

        if (!response.getStatusCode().is2xxSuccessful()) {

            throw new RuntimeException(
                    "Brevo failed: "
                            + response.getStatusCode()
            );

        }

        log.info("Brevo email sent to {}", to);

    }

}