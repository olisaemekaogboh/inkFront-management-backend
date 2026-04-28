package com.inkFront.inFront.config;

import com.inkFront.inFront.entity.BlogMedia;
import com.inkFront.inFront.entity.BlogPost;
import com.inkFront.inFront.entity.enums.BlogMediaType;
import com.inkFront.inFront.entity.enums.ContentStatus;
import com.inkFront.inFront.entity.enums.SupportedLanguage;
import com.inkFront.inFront.repository.BlogPostRepository;
import com.inkFront.inFront.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BlogDataSeeder implements CommandLineRunner {

    private final BlogPostRepository blogPostRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (blogPostRepository.count() > 0) {
            return;
        }

        createPost(
                "Why Every Growing Business Needs a Premium Website",
                "A premium website is no longer decoration. It is your business front office, sales assistant, and trust engine.",
                "<h2>Your website is your first office</h2><p>Most customers check your business online before they contact you. A strong website makes your brand look serious, trustworthy, and ready for bigger opportunities.</p><p>For modern businesses, a website should explain services, collect leads, answer questions, show proof, and guide visitors into taking action.</p>",
                "Web Strategy",
                List.of("website", "business", "branding"),
                SupportedLanguage.EN,
                true,
                1,
                "https://images.unsplash.com/photo-1497366754035-f200968a6e72?auto=format&fit=crop&w=1400&q=80",
                "https://www.youtube.com/watch?v=Zi_XLOBDo_Y"
        );

        createPost(
                "How Booking Systems Save Time for Service Businesses",
                "Manual bookings create missed calls, confusion, and lost revenue. A booking platform makes your operations cleaner.",
                "<h2>Booking should not depend on phone calls alone</h2><p>A proper booking system helps customers choose services, select dates, submit requests, and receive confirmation faster.</p><p>For car wash, transport, consulting, and home service businesses, this improves customer experience and reduces admin stress.</p>",
                "Automation",
                List.of("booking", "automation", "service-business"),
                SupportedLanguage.EN,
                true,
                2,
                "https://images.unsplash.com/photo-1551434678-e076c223a692?auto=format&fit=crop&w=1400&q=80",
                "https://www.youtube.com/watch?v=ysz5S6PUM-U"
        );

        createPost(
                "What to Put on a Business Homepage",
                "Your homepage must quickly explain what you do, who you help, and why people should trust you.",
                "<h2>Clarity wins</h2><p>A strong homepage should include a clear hero message, service overview, proof, testimonials, recent work, and a strong call to action.</p><p>Visitors should not struggle to understand your business. The page should guide them naturally from interest to action.</p>",
                "Web Design",
                List.of("homepage", "design", "conversion"),
                SupportedLanguage.EN,
                false,
                3,
                "https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=1400&q=80",
                "https://www.youtube.com/watch?v=jNQXAC9IVRw"
        );

        createPost(
                "Admin Dashboards: The Hidden Engine Behind Digital Businesses",
                "A public website attracts customers, but an admin dashboard helps the business manage everything behind the scenes.",
                "<h2>Control matters</h2><p>An admin dashboard allows your team to manage content, leads, messages, bookings, products, and reports without touching code.</p><p>This is what turns a normal website into a real business system.</p>",
                "Software",
                List.of("dashboard", "admin", "software"),
                SupportedLanguage.EN,
                false,
                4,
                "https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=1400&q=80",
                "https://www.youtube.com/watch?v=ScMzIvxBSi4"
        );

        createPost(
                "Why Your Business Needs Multilingual Content",
                "If your customers speak different languages, your website should meet them where they are.",
                "<h2>Language builds trust</h2><p>Multilingual websites make your brand more accessible and familiar to more people. English, Igbo, Hausa, and Yoruba support can help Nigerian businesses reach wider audiences.</p><p>When customers understand your message clearly, they are more likely to trust and contact you.</p>",
                "Growth",
                List.of("multilingual", "growth", "localization"),
                SupportedLanguage.EN,
                false,
                5,
                "https://images.unsplash.com/photo-1529156069898-49953e39b3ac?auto=format&fit=crop&w=1400&q=80",
                "https://www.youtube.com/watch?v=aqz-KE-bpKQ"
        );

        createPost(
                "Simple SEO Basics for Business Owners",
                "SEO helps people find your business when they search for services you offer.",
                "<h2>SEO starts with structure</h2><p>Use clear page titles, meaningful URLs, helpful content, fast loading pages, and proper headings.</p><p>You do not need to trick search engines. You need to explain your business clearly and consistently.</p>",
                "SEO",
                List.of("seo", "marketing", "visibility"),
                SupportedLanguage.EN,
                false,
                6,
                "https://images.unsplash.com/photo-1562577309-4932fdd64cd1?auto=format&fit=crop&w=1400&q=80",
                "https://www.youtube.com/watch?v=ysz5S6PUM-U"
        );

        createPost(
                "The Difference Between a Website and a Web Application",
                "A website informs. A web application helps users perform tasks.",
                "<h2>Both are useful, but they solve different problems</h2><p>A business website may show services, portfolio, and contact forms. A web application can manage bookings, users, payments, dashboards, reports, or workflows.</p><p>Many growing businesses eventually need both.</p>",
                "Software",
                List.of("website", "web-app", "development"),
                SupportedLanguage.EN,
                false,
                7,
                "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=1400&q=80",
                "https://www.youtube.com/watch?v=Zi_XLOBDo_Y"
        );

        createPost(
                "How Client Portals Improve Customer Experience",
                "Client portals give customers a secure place to track requests, documents, progress, and communication.",
                "<h2>Customers want visibility</h2><p>A client portal reduces repeated messages and gives customers confidence that their request is being handled.</p><p>This is useful for agencies, schools, consultants, builders, and service companies.</p>",
                "Customer Experience",
                List.of("client-portal", "customers", "operations"),
                SupportedLanguage.EN,
                false,
                8,
                "https://images.unsplash.com/photo-1556761175-b413da4baf72?auto=format&fit=crop&w=1400&q=80",
                "https://www.youtube.com/watch?v=jNQXAC9IVRw"
        );

        createPost(
                "How to Plan Content for a New Website",
                "Good content makes website development faster and more effective.",
                "<h2>Plan before building</h2><p>Prepare your services, business description, images, testimonials, FAQs, contact details, and calls to action before development starts.</p><p>This helps the website launch faster and look more complete.</p>",
                "Content",
                List.of("content", "planning", "website"),
                SupportedLanguage.EN,
                false,
                9,
                "https://images.unsplash.com/photo-1455390582262-044cdead277a?auto=format&fit=crop&w=1400&q=80",
                "https://www.youtube.com/watch?v=ScMzIvxBSi4"
        );

        createPost(
                "Digital Trust: Why Design Quality Affects Sales",
                "People judge your business by how your digital presence looks and feels.",
                "<h2>Design creates confidence</h2><p>Clean layouts, good spacing, readable text, fast pages, and consistent branding make people feel safer contacting your business.</p><p>Premium design is not just beauty. It is trust, clarity, and conversion.</p>",
                "Branding",
                List.of("design", "trust", "sales"),
                SupportedLanguage.EN,
                true,
                10,
                "https://images.unsplash.com/photo-1559028012-481c04fa702d?auto=format&fit=crop&w=1400&q=80",
                "https://www.youtube.com/watch?v=aqz-KE-bpKQ"
        );
    }

    private void createPost(
            String title,
            String excerpt,
            String content,
            String category,
            List<String> tags,
            SupportedLanguage language,
            boolean featured,
            int displayOrder,
            String imageUrl,
            String videoUrl
    ) {
        BlogPost post = new BlogPost();
        post.setTitle(title);
        post.setSlug(SlugUtil.toSlug(title));
        post.setExcerpt(excerpt);
        post.setContent(content);
        post.setCategory(category);
        post.setTags(tags);
        post.setLanguage(language);
        post.setStatus(ContentStatus.PUBLISHED);
        post.setFeatured(featured);
        post.setDisplayOrder(displayOrder);
        post.setAuthorName("InkFront Team");
        post.setFeaturedImageUrl(imageUrl);
        post.setVideoUrl(videoUrl);
        post.setEmbedVideoUrl(toEmbedUrl(videoUrl));
        post.setPublishedAt(LocalDateTime.now().minusDays(displayOrder));

        BlogMedia imageMedia = new BlogMedia();
        imageMedia.setMediaType(BlogMediaType.IMAGE);
        imageMedia.setMediaUrl(imageUrl);
        imageMedia.setDisplayOrder(0);
        post.addMedia(imageMedia);

        BlogMedia videoMedia = new BlogMedia();
        videoMedia.setMediaType(BlogMediaType.VIDEO);
        videoMedia.setMediaUrl(videoUrl);
        videoMedia.setDisplayOrder(1);
        post.addMedia(videoMedia);

        blogPostRepository.save(post);
    }

    private String toEmbedUrl(String videoUrl) {
        if (videoUrl == null || videoUrl.isBlank()) {
            return null;
        }

        String value = videoUrl.trim();

        if (value.contains("youtube.com/watch?v=")) {
            String videoId = value.substring(value.indexOf("watch?v=") + 8);
            int ampIndex = videoId.indexOf("&");
            if (ampIndex >= 0) {
                videoId = videoId.substring(0, ampIndex);
            }
            return "https://www.youtube.com/embed/" + videoId;
        }

        if (value.contains("youtu.be/")) {
            String videoId = value.substring(value.indexOf("youtu.be/") + 9);
            int queryIndex = videoId.indexOf("?");
            if (queryIndex >= 0) {
                videoId = videoId.substring(0, queryIndex);
            }
            return "https://www.youtube.com/embed/" + videoId;
        }

        return value;
    }
}