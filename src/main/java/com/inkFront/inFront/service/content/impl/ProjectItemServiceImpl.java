package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.content.ProjectDTO;
import com.inkFront.inFront.entity.ProjectItem;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.exception.DuplicateResourceException;
import com.inkFront.inFront.exception.InvalidRequestException;
import com.inkFront.inFront.exception.ResourceNotFoundException;
import com.inkFront.inFront.mapper.content.ProjectItemMapper;
import com.inkFront.inFront.repository.ProjectItemRepository;
import com.inkFront.inFront.service.content.ProjectItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectItemServiceImpl implements ProjectItemService {

    private final ProjectItemRepository projectItemRepository;
    private final ProjectItemMapper projectItemMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getAll() {
        return projectItemRepository
                .findAll(Sort.by(Sort.Direction.ASC, "displayOrder").and(Sort.by("id")))
                .stream()
                .map(projectItemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getById(Long id) {
        return projectItemMapper.toDto(findById(id));
    }

    @Override
    public ProjectDTO create(ProjectDTO dto) {
        ProjectItem entity = new ProjectItem();
        applyFields(dto, entity, true);
        validateSlugLanguageUnique(entity.getSlug(), entity.getLanguage(), null);

        return projectItemMapper.toDto(projectItemRepository.save(entity));
    }

    @Override
    public ProjectDTO update(Long id, ProjectDTO dto) {
        ProjectItem entity = findById(id);
        applyFields(dto, entity, false);
        validateSlugLanguageUnique(entity.getSlug(), entity.getLanguage(), id);

        return projectItemMapper.toDto(projectItemRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        projectItemRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getPublishedProjects(SupportedLanguage language, boolean featuredOnly) {
        SupportedLanguage safeLanguage = language == null ? SupportedLanguage.EN : language;

        List<ProjectItem> items = findPublishedProjects(safeLanguage, featuredOnly);

        if (items.isEmpty() && safeLanguage != SupportedLanguage.EN) {
            items = findPublishedProjects(SupportedLanguage.EN, featuredOnly);
        }

        return items.stream()
                .map(projectItemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getPublishedProjectBySlug(String slug, SupportedLanguage language) {
        String safeSlug = required(slug, "Slug is required");
        SupportedLanguage safeLanguage = language == null ? SupportedLanguage.EN : language;

        return projectItemRepository
                .findBySlugAndLanguageAndStatus(safeSlug, safeLanguage, ContentStatus.PUBLISHED)
                .or(() -> safeLanguage == SupportedLanguage.EN
                        ? java.util.Optional.empty()
                        : projectItemRepository.findBySlugAndLanguageAndStatus(
                        safeSlug,
                        SupportedLanguage.EN,
                        ContentStatus.PUBLISHED
                ))
                .map(projectItemMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Published project not found for slug: " + safeSlug
                ));
    }

    private ProjectItem findById(Long id) {
        return projectItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Project item not found with id: " + id
                ));
    }

    private List<ProjectItem> findPublishedProjects(
            SupportedLanguage language,
            boolean featuredOnly
    ) {
        if (featuredOnly) {
            return projectItemRepository.findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
                    language,
                    ContentStatus.PUBLISHED
            );
        }

        return projectItemRepository.findByLanguageAndStatusOrderByDisplayOrderAsc(
                language,
                ContentStatus.PUBLISHED
        );
    }

    private void applyFields(ProjectDTO dto, ProjectItem entity, boolean creating) {
        if (dto == null) {
            throw new InvalidRequestException("Project data is required");
        }

        entity.setTitle(required(
                first(dto.getTitle(), entity.getTitle()),
                "Project title is required"
        ));

        entity.setSlug(required(
                first(dto.getSlug(), entity.getSlug()),
                "Slug is required"
        ));

        entity.setSummary(required(
                first(dto.getSummary(), dto.getShortDescription(), entity.getSummary()),
                "Project summary is required"
        ));

        entity.setDescription(first(
                dto.getDescription(),
                dto.getFullDescription(),
                entity.getDescription()
        ));

        entity.setClientIndustry(first(
                dto.getClientIndustry(),
                entity.getClientIndustry()
        ));

        entity.setProjectType(first(
                dto.getProjectType(),
                entity.getProjectType()
        ));

        entity.setClientName(first(
                dto.getClientName(),
                entity.getClientName()
        ));

        entity.setCoverImageUrl(first(
                dto.getCoverImageUrl(),
                dto.getImageUrl(),
                entity.getCoverImageUrl()
        ));

        entity.setImageUrl(first(
                dto.getImageUrl(),
                dto.getCoverImageUrl(),
                entity.getImageUrl()
        ));

        entity.setLiveUrl(first(
                dto.getLiveUrl(),
                dto.getProjectUrl(),
                entity.getLiveUrl()
        ));

        entity.setProjectUrl(first(
                dto.getProjectUrl(),
                dto.getLiveUrl(),
                entity.getProjectUrl()
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

    private void validateSlugLanguageUnique(
            String slug,
            SupportedLanguage language,
            Long currentId
    ) {
        boolean exists = currentId == null
                ? projectItemRepository.existsBySlugAndLanguage(slug, language)
                : projectItemRepository.existsBySlugAndLanguageAndIdNot(slug, language, currentId);

        if (exists) {
            throw new DuplicateResourceException(
                    "Project slug already exists for language " + language + ": " + slug
            );
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