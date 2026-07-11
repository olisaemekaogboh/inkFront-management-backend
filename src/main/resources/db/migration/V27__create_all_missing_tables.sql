-- ==========================================
-- Migration V23: Create All Missing Tables
-- ==========================================
-- Creates blog_posts, blog_media, blog_post_tags,
-- hero_sections, and client_logos tables
-- ==========================================

-- 1. Blog Posts Table
CREATE TABLE IF NOT EXISTS blog_posts (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(180) NOT NULL UNIQUE,
    title VARCHAR(180) NOT NULL,
    excerpt VARCHAR(500),
    content TEXT,
    featured_image_url VARCHAR(255),
    video_url VARCHAR(255),
    embed_video_url VARCHAR(500),
    author_name VARCHAR(120),
    category VARCHAR(120),
    language VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    featured BOOLEAN NOT NULL DEFAULT false,
    display_order INTEGER NOT NULL DEFAULT 0,
    published_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

-- 2. Blog Media Table
CREATE TABLE IF NOT EXISTS blog_media (
    id BIGSERIAL PRIMARY KEY,
    blog_post_id BIGINT NOT NULL,
    media_type VARCHAR(50) NOT NULL,
    media_url VARCHAR(500) NOT NULL,
    alt_text VARCHAR(255),
    caption TEXT,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    CONSTRAINT fk_blog_media_post FOREIGN KEY (blog_post_id)
        REFERENCES blog_posts(id) ON DELETE CASCADE
);

-- 3. Blog Post Tags (ElementCollection)
CREATE TABLE IF NOT EXISTS blog_post_tags (
    blog_post_id BIGINT NOT NULL,
    tag VARCHAR(80) NOT NULL,

    CONSTRAINT fk_blog_post_tags_post FOREIGN KEY (blog_post_id)
        REFERENCES blog_posts(id) ON DELETE CASCADE
);

-- 4. Hero Sections
CREATE TABLE IF NOT EXISTS hero_sections (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(180) NOT NULL,
    subtitle VARCHAR(255),
    body TEXT,
    background_image_url VARCHAR(255),
    placement VARCHAR(80),
    primary_button_label VARCHAR(120),
    primary_button_url VARCHAR(255),
    secondary_button_label VARCHAR(120),
    secondary_button_url VARCHAR(255),
    language VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    featured BOOLEAN NOT NULL DEFAULT false,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100)
);

-- 5. Client Logos
CREATE TABLE IF NOT EXISTS client_logos (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(160) NOT NULL,
    logo_url VARCHAR(255) NOT NULL,
    website_url VARCHAR(255),
    language VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    featured BOOLEAN NOT NULL DEFAULT false,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),

    CONSTRAINT unique_client_name UNIQUE (name)
);

-- 6. Refresh Token Sessions (if missing)
CREATE TABLE IF NOT EXISTS refresh_token_sessions (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. Users (if missing - base user table)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    enabled BOOLEAN DEFAULT true,
    account_non_expired BOOLEAN DEFAULT true,
    account_non_locked BOOLEAN DEFAULT true,
    credentials_non_expired BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 8. Add indexes
CREATE INDEX IF NOT EXISTS idx_blog_posts_slug ON blog_posts(slug);
CREATE INDEX IF NOT EXISTS idx_blog_posts_language ON blog_posts(language);
CREATE INDEX IF NOT EXISTS idx_blog_posts_status ON blog_posts(status);
CREATE INDEX IF NOT EXISTS idx_blog_posts_featured ON blog_posts(featured) WHERE featured = true;
CREATE INDEX IF NOT EXISTS idx_blog_posts_published_at ON blog_posts(published_at);
CREATE INDEX IF NOT EXISTS idx_blog_posts_category ON blog_posts(category);
CREATE INDEX IF NOT EXISTS idx_blog_posts_author ON blog_posts(author_name);

CREATE INDEX IF NOT EXISTS idx_blog_media_post_id ON blog_media(blog_post_id);
CREATE INDEX IF NOT EXISTS idx_blog_media_type ON blog_media(media_type);
CREATE INDEX IF NOT EXISTS idx_blog_post_tags_post_id ON blog_post_tags(blog_post_id);

CREATE INDEX IF NOT EXISTS idx_hero_lang_placement_status ON hero_sections(language, placement, status);
CREATE INDEX IF NOT EXISTS idx_hero_display_order ON hero_sections(display_order);
CREATE INDEX IF NOT EXISTS idx_hero_sections_featured ON hero_sections(featured) WHERE featured = true;

CREATE INDEX IF NOT EXISTS idx_client_logos_language_status ON client_logos(language, status);
CREATE INDEX IF NOT EXISTS idx_client_logos_display_order ON client_logos(display_order);
CREATE INDEX IF NOT EXISTS idx_client_logos_featured ON client_logos(featured) WHERE featured = true;

CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_token ON refresh_token_sessions(token);
CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_user_id ON refresh_token_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_expires_at ON refresh_token_sessions(expires_at);

CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- 9. Add table comments
COMMENT ON TABLE blog_posts IS 'Blog posts and articles';
COMMENT ON TABLE blog_media IS 'Media files associated with blog posts';
COMMENT ON TABLE blog_post_tags IS 'Tags for blog posts';
COMMENT ON TABLE hero_sections IS 'Hero sections for different pages and languages';
COMMENT ON TABLE client_logos IS 'Client company logos for social proof';
COMMENT ON TABLE refresh_token_sessions IS 'Refresh token sessions for authentication';
COMMENT ON TABLE users IS 'Application users';

DO $$
BEGIN
    RAISE NOTICE 'Migration V23 completed: All missing tables created';
END $$;