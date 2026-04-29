package com.inkFront.inFront.controller.admin;

import com.inkFront.inFront.dto.content.NewsletterCampaignDTO;
import com.inkFront.inFront.dto.content.NewsletterCampaignRequestDTO;
import com.inkFront.inFront.dto.content.NewsletterSubscriberDTO;
import com.inkFront.inFront.entity.enums.NewsletterCampaignStatus;
import com.inkFront.inFront.entity.enums.NewsletterSubscriberStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.service.content.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/newsletter")
@RequiredArgsConstructor
public class AdminNewsletterController {

    private final NewsletterService newsletterService;

    @GetMapping("/subscribers")
    public ResponseEntity<Page<NewsletterSubscriberDTO>> getSubscribers(
            @RequestParam(required = false) SupportedLanguage language,
            @RequestParam(required = false) NewsletterSubscriberStatus status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 50));

        return ResponseEntity.ok(
                newsletterService.findSubscribers(language, status, search, safePage, safeSize)
        );
    }

    @PostMapping("/campaigns")
    public ResponseEntity<NewsletterCampaignDTO> createCampaign(
            @RequestBody NewsletterCampaignRequestDTO request
    ) {
        return ResponseEntity.ok(newsletterService.createCampaign(request));
    }

    @PutMapping("/campaigns/{id}")
    public ResponseEntity<NewsletterCampaignDTO> updateCampaign(
            @PathVariable Long id,
            @RequestBody NewsletterCampaignRequestDTO request
    ) {
        return ResponseEntity.ok(newsletterService.updateCampaign(id, request));
    }

    @DeleteMapping("/campaigns/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        newsletterService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/campaigns/{id}/send")
    public ResponseEntity<NewsletterCampaignDTO> sendCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(newsletterService.sendCampaign(id));
    }

    @PatchMapping("/campaigns/{id}/archive")
    public ResponseEntity<NewsletterCampaignDTO> archiveCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(newsletterService.archiveCampaign(id));
    }

    @GetMapping("/campaigns/{id}")
    public ResponseEntity<NewsletterCampaignDTO> getCampaign(@PathVariable Long id) {
        return ResponseEntity.ok(newsletterService.getCampaign(id));
    }

    @GetMapping("/campaigns")
    public ResponseEntity<Page<NewsletterCampaignDTO>> getCampaigns(
            @RequestParam(required = false) SupportedLanguage language,
            @RequestParam(required = false) NewsletterCampaignStatus status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 50));

        return ResponseEntity.ok(
                newsletterService.findCampaigns(language, status, search, safePage, safeSize)
        );
    }
}