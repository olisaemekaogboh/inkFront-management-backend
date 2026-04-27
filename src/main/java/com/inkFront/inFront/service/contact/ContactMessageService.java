package com.inkFront.inFront.service.contact;

import com.inkFront.inFront.dto.contact.ContactMessageReplyDTO;
import com.inkFront.inFront.dto.contact.ContactMessageRequestDTO;
import com.inkFront.inFront.dto.contact.ContactMessageResponseDTO;
import com.inkFront.inFront.dto.contact.ContactMessageStatsDTO;
import com.inkFront.inFront.dto.contact.ContactMessageUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactMessageService {

    ContactMessageResponseDTO submit(ContactMessageRequestDTO request);

    Page<ContactMessageResponseDTO> getAll(String status, String search, Pageable pageable);

    ContactMessageResponseDTO getById(Long id);

    ContactMessageResponseDTO update(Long id, ContactMessageUpdateDTO request);

    ContactMessageResponseDTO replyToMessage(Long id, ContactMessageReplyDTO request);

    ContactMessageStatsDTO getStats();

    void delete(Long id);
}