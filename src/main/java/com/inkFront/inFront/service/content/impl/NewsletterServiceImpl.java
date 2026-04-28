package com.inkFront.inFront.service.content.impl;

import com.inkFront.inFront.dto.content.NewsletterCampaignDTO;
import com.inkFront.inFront.dto.content.NewsletterCampaignRequestDTO;
import com.inkFront.inFront.dto.content.NewsletterSubscriberDTO;
import com.inkFront.inFront.dto.content.NewsletterSubscriberRequestDTO;
import com.inkFront.inFront.entity.NewsletterCampaign;
import com.inkFront.inFront.entity.NewsletterSubscriber;
import com.inkFront.inFront.entity.enums.NewsletterCampaignStatus;
import com.inkFront.inFront.entity.enums.NewsletterSubscriberStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.mapper.content.NewsletterMapper;
import com.inkFront.inFront.repository.NewsletterCampaignRepository;
import com.inkFront.inFront.repository.NewsletterSubscriberRepository;
import com.inkFront.inFront.service.content.NewsletterService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsletterServiceImpl implements NewsletterService {

    private static final SupportedLanguage DEFAULT_LANGUAGE = SupportedLanguage.EN;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );

    private final NewsletterSubscriberRepository subscriberRepository;
    private final NewsletterCampaignRepository campaignRepository;
    private final NewsletterMapper newsletterMapper;
    private final JavaMailSender mailSender;

    @Value("${app.mail.from:no-reply@inkfront.com}")
    private String fromEmail;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Override
    public NewsletterSubscriberDTO subscribe(NewsletterSubscriberRequestDTO request) {
        if (request == null || !StringUtils.hasText(request.getEmail())) {
            throw new IllegalArgumentException("Email is required");
        }

        String email = request.getEmail().trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Please provide a valid email address");
        }

        NewsletterSubscriber subscriber = subscriberRepository
                .findByEmailIgnoreCase(email)
                .orElseGet(NewsletterSubscriber::new);

        subscriber.setEmail(email);
        subscriber.setFullName(cleanOptional(request.getFullName()));
        subscriber.setLanguage(request.getLanguage() == null ? DEFAULT_LANGUAGE : request.getLanguage());
        subscriber.setStatus(NewsletterSubscriberStatus.ACTIVE);
        subscriber.setUnsubscribedAt(null);

        if (!StringUtils.hasText(subscriber.getUnsubscribeToken())) {
            subscriber.setUnsubscribeToken(UUID.randomUUID().toString());
        }

        if (subscriber.getSubscribedAt() == null) {
            subscriber.setSubscribedAt(LocalDateTime.now());
        }

        return newsletterMapper.toSubscriberDTO(subscriberRepository.save(subscriber));
    }

    @Override
    public NewsletterSubscriberDTO unsubscribe(String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("Unsubscribe token is required");
        }

        NewsletterSubscriber subscriber = subscriberRepository.findByUnsubscribeToken(token.trim())
                .orElseThrow(() -> new EntityNotFoundException("Subscriber not found"));

        subscriber.setStatus(NewsletterSubscriberStatus.UNSUBSCRIBED);
        subscriber.setUnsubscribedAt(LocalDateTime.now());

        return newsletterMapper.toSubscriberDTO(subscriberRepository.save(subscriber));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NewsletterSubscriberDTO> findSubscribers(
            SupportedLanguage language,
            NewsletterSubscriberStatus status,
            String search,
            int page,
            int size
    ) {
        Pageable pageable = createPageable(page, size);

        Page<NewsletterSubscriber> subscribers;

        if (StringUtils.hasText(search)) {
            String keyword = search.trim();
            subscribers = subscriberRepository.findAllByEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(
                    keyword,
                    keyword,
                    pageable
            );
        } else if (language != null && status != null) {
            subscribers = subscriberRepository.findAllByLanguageAndStatus(language, status, pageable);
        } else if (language != null) {
            subscribers = subscriberRepository.findAllByLanguage(language, pageable);
        } else if (status != null) {
            subscribers = subscriberRepository.findAllByStatus(status, pageable);
        } else {
            subscribers = subscriberRepository.findAll(pageable);
        }

        return subscribers.map(newsletterMapper::toSubscriberDTO);
    }

    @Override
    public NewsletterCampaignDTO createCampaign(NewsletterCampaignRequestDTO request) {
        NewsletterCampaign campaign = new NewsletterCampaign();
        applyCampaignRequest(campaign, request);
        campaign.setStatus(NewsletterCampaignStatus.DRAFT);
        campaign.setSentCount(0);

        return newsletterMapper.toCampaignDTO(campaignRepository.save(campaign));
    }

    @Override
    public NewsletterCampaignDTO updateCampaign(Long id, NewsletterCampaignRequestDTO request) {
        NewsletterCampaign campaign = getCampaignEntity(id);

        if (campaign.getStatus() == NewsletterCampaignStatus.SENT) {
            throw new IllegalStateException("Sent newsletters cannot be edited");
        }

        applyCampaignRequest(campaign, request);

        return newsletterMapper.toCampaignDTO(campaignRepository.save(campaign));
    }

    @Override
    public void deleteCampaign(Long id) {
        NewsletterCampaign campaign = getCampaignEntity(id);

        if (campaign.getStatus() == NewsletterCampaignStatus.SENT) {
            throw new IllegalStateException("Sent newsletters cannot be deleted. Archive it instead.");
        }

        campaignRepository.delete(campaign);
    }

    @Override
    public NewsletterCampaignDTO sendCampaign(Long id) {
        NewsletterCampaign campaign = getCampaignEntity(id);

        List<NewsletterSubscriber> subscribers =
                subscriberRepository.findAllByStatusAndLanguage(
                        NewsletterSubscriberStatus.ACTIVE,
                        campaign.getLanguage()
                );

        if (subscribers.isEmpty()) {
            throw new IllegalStateException(
                    "No active subscribers found for language: " + campaign.getLanguage()
            );
        }

        int sentCount = 0;
        StringBuilder failedEmails = new StringBuilder();

        for (NewsletterSubscriber subscriber : subscribers) {
            try {
                sendNewsletterEmail(subscriber, campaign);
                sentCount++;
            } catch (Exception ex) {
                failedEmails
                        .append(subscriber.getEmail())
                        .append(" -> ")
                        .append(ex.getMessage())
                        .append("; ");
            }
        }

        if (sentCount == 0) {
            throw new IllegalStateException(
                    "Newsletter could not be sent to any subscriber. Mail error: " + failedEmails
            );
        }

        campaign.setStatus(NewsletterCampaignStatus.SENT);
        campaign.setSentAt(LocalDateTime.now());
        campaign.setSentCount(sentCount);

        return newsletterMapper.toCampaignDTO(campaignRepository.save(campaign));
    }

    @Override
    public NewsletterCampaignDTO archiveCampaign(Long id) {
        NewsletterCampaign campaign = getCampaignEntity(id);
        campaign.setStatus(NewsletterCampaignStatus.ARCHIVED);
        return newsletterMapper.toCampaignDTO(campaignRepository.save(campaign));
    }

    @Override
    @Transactional(readOnly = true)
    public NewsletterCampaignDTO getCampaign(Long id) {
        return newsletterMapper.toCampaignDTO(getCampaignEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NewsletterCampaignDTO> findCampaigns(
            SupportedLanguage language,
            NewsletterCampaignStatus status,
            String search,
            int page,
            int size
    ) {
        Pageable pageable = createPageable(page, size);

        Page<NewsletterCampaign> campaigns;

        if (StringUtils.hasText(search)) {
            String keyword = search.trim();
            campaigns = campaignRepository.findAllBySubjectContainingIgnoreCaseOrPreviewTextContainingIgnoreCase(
                    keyword,
                    keyword,
                    pageable
            );
        } else if (language != null && status != null) {
            campaigns = campaignRepository.findAllByLanguageAndStatus(language, status, pageable);
        } else if (language != null) {
            campaigns = campaignRepository.findAllByLanguage(language, pageable);
        } else if (status != null) {
            campaigns = campaignRepository.findAllByStatus(status, pageable);
        } else {
            campaigns = campaignRepository.findAll(pageable);
        }

        return campaigns.map(newsletterMapper::toCampaignDTO);
    }

    private NewsletterCampaign getCampaignEntity(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Newsletter campaign not found with id: " + id));
    }

    private void applyCampaignRequest(NewsletterCampaign campaign, NewsletterCampaignRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Newsletter request is required");
        }

        if (!StringUtils.hasText(request.getSubject())) {
            throw new IllegalArgumentException("Subject is required");
        }

        if (!StringUtils.hasText(request.getContent())) {
            throw new IllegalArgumentException("Newsletter content is required");
        }

        campaign.setSubject(request.getSubject().trim());
        campaign.setPreviewText(cleanOptional(request.getPreviewText()));
        campaign.setContent(request.getContent());
        campaign.setImageUrl(cleanOptional(request.getImageUrl()));
        campaign.setCtaLabel(cleanOptional(request.getCtaLabel()));
        campaign.setCtaUrl(cleanOptional(request.getCtaUrl()));
        campaign.setLanguage(request.getLanguage() == null ? DEFAULT_LANGUAGE : request.getLanguage());
    }

    private Pageable createPageable(int page, int size) {
        return PageRequest.of(
                Math.max(page, 0),
                size <= 0 ? 10 : Math.min(size, 100),
                Sort.by(Sort.Order.desc("id"))
        );
    }

    private String cleanOptional(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private void sendNewsletterEmail(
            NewsletterSubscriber subscriber,
            NewsletterCampaign campaign
    ) throws MessagingException {
        String unsubscribeUrl = frontendUrl + "/newsletter/unsubscribe/" + subscriber.getUnsubscribeToken();

        String html = buildHtmlEmail(subscriber, campaign, unsubscribeUrl);

        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(subscriber.getEmail());
        helper.setSubject(campaign.getSubject());
        helper.setText(html, true);

        mailSender.send(message);
    }

    private String buildHtmlEmail(
            NewsletterSubscriber subscriber,
            NewsletterCampaign campaign,
            String unsubscribeUrl
    ) {
        String name = StringUtils.hasText(subscriber.getFullName())
                ? subscriber.getFullName()
                : "there";

        String image = StringUtils.hasText(campaign.getImageUrl())
                ? "<img src=\"" + campaign.getImageUrl() + "\" alt=\"\" style=\"width:100%;border-radius:18px;margin:18px 0;\" />"
                : "";

        String cta = StringUtils.hasText(campaign.getCtaUrl()) && StringUtils.hasText(campaign.getCtaLabel())
                ? "<p style=\"margin:28px 0;\"><a href=\"" + campaign.getCtaUrl() + "\" style=\"background:#2563eb;color:#fff;padding:13px 18px;border-radius:999px;text-decoration:none;font-weight:800;\">" + campaign.getCtaLabel() + "</a></p>"
                : "";

        return """
                <!doctype html>
                <html>
                <body style="margin:0;background:#f8fafc;font-family:Arial,sans-serif;color:#111827;">
                  <div style="max-width:680px;margin:0 auto;padding:28px;">
                    <div style="background:#ffffff;border:1px solid #e5e7eb;border-radius:24px;padding:28px;">
                      <p style="color:#2563eb;font-weight:800;letter-spacing:.08em;text-transform:uppercase;font-size:12px;">InkFront Newsletter</p>
                      <h1 style="font-size:30px;line-height:1.1;margin:0 0 12px;">%s</h1>
                      <p style="color:#64748b;line-height:1.7;">Hi %s,</p>
                      %s
                      <div style="font-size:16px;line-height:1.8;color:#111827;">%s</div>
                      %s
                      <hr style="border:none;border-top:1px solid #e5e7eb;margin:28px 0;" />
                      <p style="font-size:12px;color:#64748b;line-height:1.6;">
                        You received this email because you subscribed to InkFront updates.
                        <a href="%s" style="color:#2563eb;">Unsubscribe</a>
                      </p>
                    </div>
                  </div>
                </body>
                </html>
                """.formatted(
                campaign.getSubject(),
                name,
                image,
                campaign.getContent(),
                cta,
                unsubscribeUrl
        );
    }
}