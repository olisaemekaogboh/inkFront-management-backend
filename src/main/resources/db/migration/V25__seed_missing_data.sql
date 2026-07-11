-- ==========================================
-- Migration V26: Seed Missing Data
-- ==========================================
-- Seeds data into tables that were missing
-- ==========================================

-- Seed hero_sections
INSERT INTO hero_sections (
    title, subtitle, body, background_image_url, placement,
    primary_button_label, primary_button_url,
    secondary_button_label, secondary_button_url,
    language, status, featured, display_order,
    created_at, updated_at
) VALUES (
    'Build. Launch. Grow with InFront',
    'Digital products, websites, automation, and brand systems for ambitious businesses.',
    'We help businesses design strong online platforms, launch products faster, attract better customers, and manage growth with modern technology.',
    'https://images.unsplash.com/photo-1497366754035-f200968a6e72',
    'HOME',
    'Start a Project',
    '/contact',
    'View Services',
    '/services',
    'EN',
    'PUBLISHED',
    true,
    1,
    NOW(),
    NOW()
) ON CONFLICT DO NOTHING;

-- Seed client_logos
INSERT INTO client_logos (
    name, logo_url, website_url, language, status, featured, display_order,
    created_at, updated_at
) VALUES
(
    'GrowthBridge Consult',
    'https://dummyimage.com/240x90/111827/ffffff&text=GrowthBridge',
    'https://example.com',
    'EN',
    'PUBLISHED',
    true,
    1,
    NOW(),
    NOW()
),
(
    'Prime Logistics',
    'https://dummyimage.com/240x90/111827/ffffff&text=Prime+Logistics',
    'https://example.com',
    'EN',
    'PUBLISHED',
    true,
    2,
    NOW(),
    NOW()
),
(
    'BrightPath Academy',
    'https://dummyimage.com/240x90/111827/ffffff&text=BrightPath',
    'https://example.com',
    'EN',
    'PUBLISHED',
    true,
    3,
    NOW(),
    NOW()
),
(
    'Nexa Stores',
    'https://dummyimage.com/240x90/111827/ffffff&text=Nexa+Stores',
    'https://example.com',
    'EN',
    'PUBLISHED',
    true,
    4,
    NOW(),
    NOW()
) ON CONFLICT (name) DO NOTHING;

-- Seed a sample blog post (optional)
INSERT INTO blog_posts (
    title, slug, excerpt, content, language, status,
    featured, display_order, created_at, updated_at
) VALUES (
    'Welcome to Our Blog',
    'welcome-to-our-blog',
    'Welcome to the InFront blog where we share insights about digital products and business growth.',
    'Welcome to our blog! Here we share insights about website development, business automation, and digital strategy.',
    'EN',
    'PUBLISHED',
    true,
    1,
    NOW(),
    NOW()
) ON CONFLICT (slug) DO NOTHING;