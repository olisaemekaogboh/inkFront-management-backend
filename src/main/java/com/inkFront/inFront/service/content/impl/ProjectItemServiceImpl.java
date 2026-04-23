package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.content.ProjectDTO;
import com.inkFront.inFront.entity.ProjectItem;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
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
        return projectItemRepository.findAll(Sort.by(Sort.Direction.ASC, "displayOrder"))
                .stream()
                .map(projectItemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getById(Long id) {
        ProjectItem entity = projectItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project item not found with id: " + id));
        return projectItemMapper.toDto(entity);
    }

    @Override
    public ProjectDTO create(ProjectDTO dto) {
        ProjectItem entity = projectItemMapper.toEntity(dto);

        if (!StringUtils.hasText(entity.getSlug())) {
            throw new IllegalArgumentException("Slug is required");
        }

        if (projectItemRepository.existsBySlug(entity.getSlug())) {
            throw new IllegalArgumentException("Project slug already exists: " + entity.getSlug());
        }

        if (entity.getFeatured() == null) {
            entity.setFeatured(false);
        }

        if (entity.getDisplayOrder() == null) {
            entity.setDisplayOrder(0);
        }

        if (entity.getStatus() == null) {
            entity.setStatus(ContentStatus.DRAFT);
        }

        ProjectItem saved = projectItemRepository.save(entity);
        return projectItemMapper.toDto(saved);
    }

    @Override
    public ProjectDTO update(Long id, ProjectDTO dto) {
        ProjectItem entity = projectItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project item not found with id: " + id));

        projectItemMapper.updateEntityFromDto(dto, entity);

        if (!StringUtils.hasText(entity.getSlug())) {
            throw new IllegalArgumentException("Slug is required");
        }

        if (projectItemRepository.existsBySlugAndIdNot(entity.getSlug(), id)) {
            throw new IllegalArgumentException("Project slug already exists: " + entity.getSlug());
        }

        if (entity.getFeatured() == null) {
            entity.setFeatured(false);
        }

        if (entity.getDisplayOrder() == null) {
            entity.setDisplayOrder(0);
        }

        if (entity.getStatus() == null) {
            entity.setStatus(ContentStatus.DRAFT);
        }

        ProjectItem saved = projectItemRepository.save(entity);
        return projectItemMapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!projectItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project item not found with id: " + id);
        }
        projectItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getPublishedProjects(SupportedLanguage language, boolean featuredOnly) {
        List<ProjectItem> items = featuredOnly
                ? projectItemRepository.findByLanguageAndStatusAndFeaturedTrueOrderByDisplayOrderAsc(
                language,
                ContentStatus.PUBLISHED
        )
                : projectItemRepository.findByLanguageAndStatusOrderByDisplayOrderAsc(
                language,
                ContentStatus.PUBLISHED
        );

        return items.stream()
                .map(projectItemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getPublishedProjectBySlug(String slug, SupportedLanguage language) {
        ProjectItem entity = projectItemRepository.findBySlugAndLanguageAndStatus(
                        slug,
                        language,
                        ContentStatus.PUBLISHED
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Published project not found for slug: " + slug + " and language: " + language
                ));

        return projectItemMapper.toDto(entity);
    }
}