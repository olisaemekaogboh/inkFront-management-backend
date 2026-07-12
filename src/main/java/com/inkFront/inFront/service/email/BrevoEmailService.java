package com.inkFront.inFront.service.email;

public interface BrevoEmailService {

    void sendTextEmail(
            String to,
            String subject,
            String textContent,
            String replyTo
    );

    void sendHtmlEmail(
            String to,
            String subject,
            String htmlContent,
            String replyTo
    );

}