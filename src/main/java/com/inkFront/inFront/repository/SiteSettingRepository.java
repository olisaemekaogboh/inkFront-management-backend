package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.SiteSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteSettingRepository extends JpaRepository<SiteSetting, Long> {

    List<SiteSetting> findByLanguageIgnoreCaseAndSettingGroupIgnoreCaseAndStatusIgnoreCaseOrderByDisplayOrderAscSettingKeyAsc(
            String language,
            String settingGroup,
            String status
    );
}