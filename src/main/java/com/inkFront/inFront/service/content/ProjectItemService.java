package com.inkFront.inFront.service.content;

import com.inkFront.inFront.dto.content.ProjectDTO;
import com.inkFront.inFront.entity.enums.SupportedLanguage;

import java.util.List;

public interface ProjectItemService {

    List<ProjectDTO> getAll();

    ProjectDTO getById(Long id);

    ProjectDTO create(ProjectDTO dto);

    ProjectDTO update(Long id, ProjectDTO dto);

    void delete(Long id);

    List<ProjectDTO> getPublishedProjects(SupportedLanguage language, boolean featuredOnly);

    ProjectDTO getPublishedProjectBySlug(String slug, SupportedLanguage language);
}