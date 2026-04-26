package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.contact.ContactMessageResponseDTO;
import com.inkFront.inFront.service.contact.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/contact-messages")
@RequiredArgsConstructor
public class AdminContactMessageController {

    private final ContactMessageService contactMessageService;

    @GetMapping
    public Page<ContactMessageResponseDTO> getMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return contactMessageService.getAll(PageRequest.of(page, size));
    }

    @PatchMapping("/{id}/status")
    public ContactMessageResponseDTO updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        return contactMessageService.updateStatus(
                id,
                request.get("status"),
                request.get("adminNote")
        );
    }
}