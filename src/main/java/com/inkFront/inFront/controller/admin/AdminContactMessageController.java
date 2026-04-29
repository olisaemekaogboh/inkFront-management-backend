package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.contact.ContactMessageReplyDTO;
import com.inkFront.inFront.dto.contact.ContactMessageResponseDTO;
import com.inkFront.inFront.dto.contact.ContactMessageStatsDTO;
import com.inkFront.inFront.dto.contact.ContactMessageUpdateDTO;
import com.inkFront.inFront.service.contact.ContactMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/contact-messages")
@RequiredArgsConstructor
public class AdminContactMessageController {

    private final ContactMessageService contactMessageService;

    @GetMapping
    public ResponseEntity<Page<ContactMessageResponseDTO>> getMessages(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 50));

        Pageable pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return ResponseEntity.ok(contactMessageService.getAll(status, search, pageable));
    }

    @GetMapping("/stats")
    public ResponseEntity<ContactMessageStatsDTO> getStats() {
        return ResponseEntity.ok(contactMessageService.getStats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactMessageResponseDTO> getMessageById(@PathVariable Long id) {
        return ResponseEntity.ok(contactMessageService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContactMessageResponseDTO> updateMessage(
            @PathVariable Long id,
            @RequestBody ContactMessageUpdateDTO request
    ) {
        return ResponseEntity.ok(contactMessageService.update(id, request));
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<ContactMessageResponseDTO> replyToMessage(
            @PathVariable Long id,
            @Valid @RequestBody ContactMessageReplyDTO request
    ) {
        return ResponseEntity.ok(contactMessageService.replyToMessage(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        contactMessageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}