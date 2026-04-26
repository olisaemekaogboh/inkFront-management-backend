CREATE TABLE IF NOT EXISTS site_settings (
    id BIGSERIAL PRIMARY KEY,
    setting_group VARCHAR(80) NOT NULL,
    setting_key VARCHAR(120) NOT NULL,
    setting_value TEXT,
    value_type VARCHAR(40) NOT NULL DEFAULT 'TEXT',
    language VARCHAR(10) NOT NULL DEFAULT 'EN',
    status VARCHAR(30) NOT NULL DEFAULT 'PUBLISHED',
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_site_settings_language_group_key UNIQUE (language, setting_group, setting_key)
);

CREATE INDEX IF NOT EXISTS idx_site_settings_language_group
ON site_settings(language, setting_group);

CREATE INDEX IF NOT EXISTS idx_site_settings_status
ON site_settings(status);

CREATE TABLE IF NOT EXISTS faqs (
    id BIGSERIAL PRIMARY KEY,
    page_key VARCHAR(80) NOT NULL,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    language VARCHAR(10) NOT NULL DEFAULT 'EN',
    status VARCHAR(30) NOT NULL DEFAULT 'PUBLISHED',
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_faqs_language_page
ON faqs(language, page_key);

CREATE INDEX IF NOT EXISTS idx_faqs_status
ON faqs(status);

INSERT INTO site_settings (
    setting_group,
    setting_key,
    setting_value,
    value_type,
    language,
    status,
    display_order,
    created_at,
    updated_at
)
VALUES
(
    'ABOUT',
    'heroTitle',
    'We build digital systems that help businesses look trusted and grow online.',
    'TEXT',
    'EN',
    'PUBLISHED',
    1,
    NOW(),
    NOW()
),
(
    'ABOUT',
    'heroSubtitle',
    'InkFront creates websites, product pages, client portals, dashboards, and business tools for modern brands.',
    'TEXT',
    'EN',
    'PUBLISHED',
    2,
    NOW(),
    NOW()
),
(
    'ABOUT',
    'missionTitle',
    'Our mission',
    'TEXT',
    'EN',
    'PUBLISHED',
    3,
    NOW(),
    NOW()
),
(
    'ABOUT',
    'missionText',
    'We help businesses move from scattered ideas and weak online presence to polished digital platforms that communicate value clearly.',
    'TEXT',
    'EN',
    'PUBLISHED',
    4,
    NOW(),
    NOW()
),
(
    'ABOUT',
    'visionTitle',
    'Our vision',
    'TEXT',
    'EN',
    'PUBLISHED',
    5,
    NOW(),
    NOW()
),
(
    'ABOUT',
    'visionText',
    'To become a trusted digital partner for businesses that want practical software, strong design, and scalable web systems.',
    'TEXT',
    'EN',
    'PUBLISHED',
    6,
    NOW(),
    NOW()
),
(
    'ABOUT',
    'imageUrl',
    'https://images.unsplash.com/photo-1551434678-e076c223a692?auto=format&fit=crop&w=1400&q=80',
    'IMAGE_URL',
    'EN',
    'PUBLISHED',
    7,
    NOW(),
    NOW()
)
ON CONFLICT (language, setting_group, setting_key)
DO UPDATE SET
    setting_value = EXCLUDED.setting_value,
    value_type = EXCLUDED.value_type,
    status = EXCLUDED.status,
    display_order = EXCLUDED.display_order,
    updated_at = NOW();

INSERT INTO faqs (
    page_key,
    question,
    answer,
    language,
    status,
    display_order,
    created_at,
    updated_at
)
VALUES
(
    'ABOUT',
    'What does InkFront build?',
    'InkFront builds websites, product pages, business platforms, client portals, admin dashboards, booking systems, and custom digital tools.',
    'EN',
    'PUBLISHED',
    1,
    NOW(),
    NOW()
),
(
    'ABOUT',
    'Do you work with existing projects?',
    'Yes. We can improve existing Spring Boot and React projects without starting from scratch, while preserving working authentication, APIs, and admin features.',
    'EN',
    'PUBLISHED',
    2,
    NOW(),
    NOW()
),
(
    'ABOUT',
    'Can backend content control public pages?',
    'Yes. Public pages should pull published content from backend APIs, while admin pages manage the same content.',
    'EN',
    'PUBLISHED',
    3,
    NOW(),
    NOW()
),
(
    'ABOUT',
    'Do you support multilingual websites?',
    'Yes. InkFront projects can support English, Igbo, Hausa, and Yoruba depending on the content available in the backend.',
    'EN',
    'PUBLISHED',
    4,
    NOW(),
    NOW()
);