package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.contact.ContactMessageResponseDTO;
import com.inkFront.inFront.dto.contact.ContactMessageStatsDTO;
import com.inkFront.inFront.dto.contact.ContactMessageUpdateDTO;
import com.inkFront.inFront.service.contact.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/contact-messages")
@RequiredArgsConstructor
public class AdminContactMessageController {

    private final ContactMessageService contactMessageService;

    @GetMapping
    public Page<ContactMessageResponseDTO> getMessages(
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return contactMessageService.getAll(
                status,
                PageRequest.of(Math.max(page, 0), Math.max(1, Math.min(size, 100)))
        );
    }

    @GetMapping("/stats")
    public ContactMessageStatsDTO getStats() {
        return contactMessageService.getStats();
    }

    @GetMapping("/{id}")
    public ContactMessageResponseDTO getMessage(@PathVariable Long id) {
        return contactMessageService.getById(id);
    }

    @PatchMapping("/{id}")
    public ContactMessageResponseDTO updateMessage(
            @PathVariable Long id,
            @RequestBody ContactMessageUpdateDTO request
    ) {
        return contactMessageService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        contactMessageService.delete(id);
    }
}