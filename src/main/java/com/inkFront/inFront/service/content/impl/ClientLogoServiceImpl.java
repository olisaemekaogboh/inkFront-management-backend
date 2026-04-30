package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.api.PageResponse;
import com.inkFront.inFront.dto.content.ClientLogoDTO;
import com.inkFront.inFront.entity.ClientLogo;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.exception.InvalidRequestException;
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
import org.springframework.util.StringUtils;

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
        return clientLogoRepository
                .findAll(Sort.by(Sort.Direction.ASC, "displayOrder").and(Sort.by("id")))
                .stream()
                .map(clientLogoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientLogoDTO getById(Long id) {
        return clientLogoMapper.toDto(findById(id));
    }

    @Override
    public ClientLogoDTO create(ClientLogoDTO dto) {
        ClientLogo entity = new ClientLogo();
        applyFields(dto, entity, true);
        return clientLogoMapper.toDto(clientLogoRepository.save(entity));
    }

    @Override
    public ClientLogoDTO update(Long id, ClientLogoDTO dto) {
        ClientLogo entity = findById(id);
        applyFields(dto, entity, false);
        return clientLogoMapper.toDto(clientLogoRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        clientLogoRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClientLogoDTO> getPublishedClientLogos(
            SupportedLanguage language,
            boolean featuredOnly,
            Pageable pageable
    ) {
        SupportedLanguage safeLanguage = language == null ? SupportedLanguage.EN : language;

        Page<ClientLogo> page = findPublishedClientLogos(safeLanguage, featuredOnly, pageable);

        if (page.isEmpty() && safeLanguage != SupportedLanguage.EN) {
            page = findPublishedClientLogos(SupportedLanguage.EN, featuredOnly, pageable);
        }

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

    private ClientLogo findById(Long id) {
        return clientLogoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client logo not found with id: " + id
                ));
    }

    private Page<ClientLogo> findPublishedClientLogos(
            SupportedLanguage language,
            boolean featuredOnly,
            Pageable pageable
    ) {
        if (featuredOnly) {
            return clientLogoRepository.findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
                    language,
                    ContentStatus.PUBLISHED,
                    pageable
            );
        }

        return clientLogoRepository.findByLanguageAndStatusOrderByDisplayOrderAsc(
                language,
                ContentStatus.PUBLISHED,
                pageable
        );
    }

    private void applyFields(ClientLogoDTO dto, ClientLogo entity, boolean creating) {
        if (dto == null) {
            throw new InvalidRequestException("Client logo data is required");
        }

        entity.setName(required(
                first(dto.getName(), entity.getName()),
                "Client name is required"
        ));

        entity.setLogoUrl(required(
                first(dto.getLogoUrl(), entity.getLogoUrl()),
                "Logo URL is required"
        ));

        entity.setWebsiteUrl(first(
                dto.getWebsiteUrl(),
                entity.getWebsiteUrl()
        ));

        if (dto.getLanguage() != null) {
            entity.setLanguage(dto.getLanguage());
        } else if (entity.getLanguage() == null) {
            entity.setLanguage(SupportedLanguage.EN);
        }

        if (dto.getDisplayOrder() != null) {
            entity.setDisplayOrder(dto.getDisplayOrder());
        } else if (dto.getSortOrder() != null) {
            entity.setDisplayOrder(dto.getSortOrder());
        } else if (entity.getDisplayOrder() == null) {
            entity.setDisplayOrder(0);
        }

        if (dto.getFeatured() != null) {
            entity.setFeatured(dto.getFeatured());
        } else if (entity.getFeatured() == null) {
            entity.setFeatured(false);
        }

        if (dto.getActive() != null) {
            entity.setStatus(Boolean.TRUE.equals(dto.getActive())
                    ? ContentStatus.PUBLISHED
                    : ContentStatus.DRAFT);
        } else if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        } else if (entity.getStatus() == null) {
            entity.setStatus(creating ? ContentStatus.DRAFT : ContentStatus.DRAFT);
        }
    }

    private String first(String... values) {
        if (values == null) return null;

        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value.trim();
            }
        }

        return null;
    }

    private String required(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new InvalidRequestException(message);
        }

        return value.trim();
    }
}