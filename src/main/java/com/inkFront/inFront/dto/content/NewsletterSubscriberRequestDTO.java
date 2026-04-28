package com.inkFront.inFront.dto.content;

import com.inkFront.inFront.entity.enums.SupportedLanguage;

public class NewsletterSubscriberRequestDTO {

    private String email;
    private String fullName;
    private SupportedLanguage language;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public SupportedLanguage getLanguage() { return language; }
    public void setLanguage(SupportedLanguage language) { this.language = language; }
}