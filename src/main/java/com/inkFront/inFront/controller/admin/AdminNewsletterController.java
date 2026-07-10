package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.NewsletterCampaignDTO;
import com.inkFront.inFront.dto.content.NewsletterCampaignRequestDTO;
import com.inkFront.inFront.dto.content.NewsletterSubscriberDTO;
import com.inkFront.inFront.entity.enums.NewsletterCampaignStatus;
import com.inkFront.inFront.entity.enums.NewsletterSubscriberStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.service.content.NewsletterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/newsletter")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminNewsletterController {

    private final NewsletterService newsletterService;

    @GetMapping("/subscribers")
    public ResponseEntity<Page<NewsletterSubscriberDTO>> getSubscribers(

            @RequestParam(required = false)
            SupportedLanguage language,

            @RequestParam(required = false)
            NewsletterSubscriberStatus status,

            @RequestParam(required = false)
            String search,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size

    ) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 50));

        return ResponseEntity.ok(
                newsletterService.findSubscribers(
                        language,
                        status,
                        search,
                        safePage,
                        safeSize
                )
        );
    }

    @PostMapping("/campaigns")
    public ResponseEntity<NewsletterCampaignDTO> createCampaign(
            @Valid @RequestBody NewsletterCampaignRequestDTO request
    ) {

        log.info("Creating newsletter campaign: {}", request.getSubject());

        NewsletterCampaignDTO campaign =
                newsletterService.createCampaign(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(campaign);
    }

    @PutMapping("/campaigns/{id}")
    public ResponseEntity<NewsletterCampaignDTO> updateCampaign(

            @PathVariable @Positive Long id,

            @Valid
            @RequestBody NewsletterCampaignRequestDTO request

    ) {

        log.info("Updating newsletter campaign {}", id);

        return ResponseEntity.ok(
                newsletterService.updateCampaign(id, request)
        );
    }

    @DeleteMapping("/campaigns/{id}")
    public ResponseEntity<Void> deleteCampaign(
            @PathVariable @Positive Long id
    ) {

        log.info("Deleting newsletter campaign {}", id);

        newsletterService.deleteCampaign(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/campaigns/{id}/send")
    public ResponseEntity<NewsletterCampaignDTO> sendCampaign(
            @PathVariable @Positive Long id
    ) {

        log.info("Sending newsletter campaign {}", id);

        return ResponseEntity.ok(
                newsletterService.sendCampaign(id)
        );
    }

    @PatchMapping("/campaigns/{id}/archive")
    public ResponseEntity<NewsletterCampaignDTO> archiveCampaign(
            @PathVariable @Positive Long id
    ) {

        log.info("Archiving newsletter campaign {}", id);

        return ResponseEntity.ok(
                newsletterService.archiveCampaign(id)
        );
    }

    @GetMapping("/campaigns/{id}")
    public ResponseEntity<NewsletterCampaignDTO> getCampaign(
            @PathVariable @Positive Long id
    ) {

        return ResponseEntity.ok(
                newsletterService.getCampaign(id)
        );
    }

    @GetMapping("/campaigns")
    public ResponseEntity<Page<NewsletterCampaignDTO>> getCampaigns(

            @RequestParam(required = false)
            SupportedLanguage language,

            @RequestParam(required = false)
            NewsletterCampaignStatus status,

            @RequestParam(required = false)
            String search,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size

    ) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 50));

        return ResponseEntity.ok(
                newsletterService.findCampaigns(
                        language,
                        status,
                        search,
                        safePage,
                        safeSize
                )
        );
    }
}