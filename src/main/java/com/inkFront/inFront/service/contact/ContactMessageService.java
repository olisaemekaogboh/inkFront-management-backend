package com.inkFront.inFront.service.contact;

import com.inkFront.inFront.dto.contact.ContactMessageRequestDTO;
import com.inkFront.inFront.dto.contact.ContactMessageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactMessageService {

    ContactMessageResponseDTO submit(ContactMessageRequestDTO request);

    Page<ContactMessageResponseDTO> getAll(Pageable pageable);

    ContactMessageResponseDTO updateStatus(Long id, String status, String adminNote);
}