package com.inkFront.inFront.service.contact;

import com.inkFront.inFront.entity.ContactMessage;

public interface ContactNotificationService {

    void notifyAdmin(ContactMessage message);
}