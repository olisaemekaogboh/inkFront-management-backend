package com.inkFront.inFront.controller.publicapi;

import com.inkFront.inFront.dto.contact.ContactMessageRequestDTO;
import com.inkFront.inFront.dto.contact.ContactMessageResponseDTO;
import com.inkFront.inFront.service.contact.ContactMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/contact-messages")
@RequiredArgsConstructor
public class PublicContactController {

    private final ContactMessageService contactMessageService;

    @PostMapping
    public ContactMessageResponseDTO submitMessage(
            @Valid @RequestBody ContactMessageRequestDTO request
    ) {
        return contactMessageService.submit(request);
    }
}