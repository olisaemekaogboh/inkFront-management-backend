-- ==========================================
-- Migration V22: Create Blog Tables
-- ==========================================
-- Creates blog_posts and blog_media tables
-- Based on JPA entities
-- ==========================================

-- 1. Create blog_posts table
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

-- 2. Create blog_media table
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

-- 3. Create blog_post_tags table (for ElementCollection)
CREATE TABLE IF NOT EXISTS blog_post_tags (
    blog_post_id BIGINT NOT NULL,
    tag VARCHAR(80) NOT NULL,

    CONSTRAINT fk_blog_post_tags_post FOREIGN KEY (blog_post_id)
        REFERENCES blog_posts(id) ON DELETE CASCADE
);

-- 4. Add indexes
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

-- 5. Add comments
COMMENT ON TABLE blog_posts IS 'Blog posts and articles';
COMMENT ON TABLE blog_media IS 'Media files associated with blog posts';
COMMENT ON TABLE blog_post_tags IS 'Tags for blog posts';