package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.api.PageResponse;
import com.inkFront.inFront.dto.content.ClientLogoDTO;
import com.inkFront.inFront.entity.ClientLogo;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.exception.ResourceNotFoundException;
import com.inkFront.inFront.mapper.content.ClientLogoMapper;
import com.inkFront.inFront.repository.ClientLogoRepository;
import com.inkFront.inFront.service.content.ClientLogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientLogoServiceImpl implements ClientLogoService {

    private final ClientLogoRepository clientLogoRepository;
    private final ClientLogoMapper clientLogoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ClientLogoDTO> getAll() {
        return clientLogoRepository.findAll(Sort.by(Sort.Direction.ASC, "displayOrder"))
                .stream()
                .map(clientLogoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientLogoDTO getById(Long id) {
        ClientLogo entity = clientLogoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client logo not found with id: " + id));
        return clientLogoMapper.toDto(entity);
    }

    @Override
    public ClientLogoDTO create(ClientLogoDTO dto) {
        ClientLogo entity = clientLogoMapper.toEntity(dto);

        if (entity.getFeatured() == null) {
            entity.setFeatured(false);
        }
        if (entity.getDisplayOrder() == null) {
            entity.setDisplayOrder(0);
        }
        if (entity.getStatus() == null) {
            entity.setStatus(ContentStatus.DRAFT);
        }

        ClientLogo saved = clientLogoRepository.save(entity);
        return clientLogoMapper.toDto(saved);
    }

    @Override
    public ClientLogoDTO update(Long id, ClientLogoDTO dto) {
        ClientLogo entity = clientLogoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client logo not found with id: " + id));

        clientLogoMapper.updateEntityFromDto(dto, entity);

        ClientLogo saved = clientLogoRepository.save(entity);
        return clientLogoMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!clientLogoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client logo not found with id: " + id);
        }
        clientLogoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClientLogoDTO> getPublishedClientLogos(
            SupportedLanguage language,
            boolean featuredOnly,
            Pageable pageable
    ) {
        Page<ClientLogo> page = featuredOnly
                ? clientLogoRepository.findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
                language, ContentStatus.PUBLISHED, pageable
        )
                : clientLogoRepository.findByLanguageAndStatusOrderByDisplayOrderAsc(
                language, ContentStatus.PUBLISHED, pageable
        );

        return PageResponse.<ClientLogoDTO>builder()
                .content(page.getContent().stream().map(clientLogoMapper::toDto).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}