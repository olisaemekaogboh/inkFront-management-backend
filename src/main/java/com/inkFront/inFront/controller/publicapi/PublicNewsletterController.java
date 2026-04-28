package com.inkFront.inFront.controller.publicapi;

import com.inkFront.inFront.dto.content.NewsletterSubscriberDTO;
import com.inkFront.inFront.dto.content.NewsletterSubscriberRequestDTO;
import com.inkFront.inFront.service.content.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/newsletter")
@RequiredArgsConstructor
public class PublicNewsletterController {

    private final NewsletterService newsletterService;

    @PostMapping("/subscribe")
    public ResponseEntity<NewsletterSubscriberDTO> subscribe(
            @RequestBody NewsletterSubscriberRequestDTO request
    ) {
        return ResponseEntity.ok(newsletterService.subscribe(request));
    }

    @PatchMapping("/unsubscribe/{token}")
    public ResponseEntity<NewsletterSubscriberDTO> unsubscribe(
            @PathVariable String token
    ) {
        return ResponseEntity.ok(newsletterService.unsubscribe(token));
    }
}