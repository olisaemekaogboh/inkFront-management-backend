CREATE TABLE IF NOT EXISTS newsletter_subscribers (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(180) NOT NULL UNIQUE,
    full_name VARCHAR(120),
    language VARCHAR(20) NOT NULL DEFAULT 'EN',
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    unsubscribe_token VARCHAR(80) NOT NULL UNIQUE,
    subscribed_at TIMESTAMP,
    unsubscribed_at TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS newsletter_campaigns (
    id BIGSERIAL PRIMARY KEY,
    subject VARCHAR(180) NOT NULL,
    preview_text VARCHAR(500),
    content TEXT,
    image_url VARCHAR(255),
    cta_label VARCHAR(255),
    cta_url VARCHAR(500),
    language VARCHAR(20) NOT NULL DEFAULT 'EN',
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    sent_at TIMESTAMP,
    sent_count INTEGER DEFAULT 0,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_newsletter_subscribers_email
    ON newsletter_subscribers (email);

CREATE INDEX IF NOT EXISTS idx_newsletter_subscribers_status_language
    ON newsletter_subscribers (status, language);

CREATE INDEX IF NOT EXISTS idx_newsletter_campaigns_status_language
    ON newsletter_campaigns (status, language);