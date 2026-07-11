-- ==========================================
-- Migration V27: Create All Missing Tables
-- ==========================================
-- Creates tables that were missing from earlier migrations
-- Automatically detects existing columns before creating indexes
-- ==========================================

-- ==========================================
-- PART 1: Create Missing Tables
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
    updated_by VARCHAR(100)
);

-- 3. Blog Post Tags (ElementCollection)
CREATE TABLE IF NOT EXISTS blog_post_tags (
    blog_post_id BIGINT NOT NULL,
    tag VARCHAR(80) NOT NULL
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

-- 6. Users Table (if missing)
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

-- 7. Contact Messages - Add missing columns
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'public'
        AND table_name = 'contact_messages'
    ) THEN
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'contact_messages'
            AND column_name = 'admin_note'
        ) THEN
            ALTER TABLE contact_messages ADD COLUMN admin_note TEXT;
            RAISE NOTICE 'Added admin_note column to contact_messages';
        END IF;

        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'contact_messages'
            AND column_name = 'subject'
        ) THEN
            ALTER TABLE contact_messages ADD COLUMN subject VARCHAR(255);
            RAISE NOTICE 'Added subject column to contact_messages';
        END IF;

        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'contact_messages'
            AND column_name = 'is_read'
        ) THEN
            ALTER TABLE contact_messages ADD COLUMN is_read BOOLEAN DEFAULT false;
            RAISE NOTICE 'Added is_read column to contact_messages';
        END IF;
    END IF;
END $$;

-- ==========================================
-- PART 2: Add Foreign Key Constraints
-- ==========================================

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'blog_posts')
    AND EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'blog_media') THEN
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.constraint_column_usage
            WHERE constraint_name = 'fk_blog_media_post'
        ) THEN
            ALTER TABLE blog_media
            ADD CONSTRAINT fk_blog_media_post
            FOREIGN KEY (blog_post_id) REFERENCES blog_posts(id) ON DELETE CASCADE;
        END IF;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'blog_posts')
    AND EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'blog_post_tags') THEN
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.constraint_column_usage
            WHERE constraint_name = 'fk_blog_post_tags_post'
        ) THEN
            ALTER TABLE blog_post_tags
            ADD CONSTRAINT fk_blog_post_tags_post
            FOREIGN KEY (blog_post_id) REFERENCES blog_posts(id) ON DELETE CASCADE;
        END IF;
    END IF;
END $$;

-- ==========================================
-- PART 3: Create Indexes - SAFELY with column detection
-- ==========================================

-- Blog indexes
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

CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- ==========================================
-- SAFELY CREATE REFRESH_TOKEN_SESSIONS INDEXES
-- ==========================================
DO $$
DECLARE
    col_name text;
BEGIN
    -- Check if refresh_token_sessions table exists
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'public'
        AND table_name = 'refresh_token_sessions'
    ) THEN
        RAISE NOTICE 'refresh_token_sessions table exists, checking columns...';

        -- Check for token column
        IF EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'token'
        ) THEN
            CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_token
            ON refresh_token_sessions(token);
            RAISE NOTICE '✅ Created index on token column';
        ELSE
            RAISE NOTICE '⚠️ Column "token" does not exist in refresh_token_sessions';
        END IF;

        -- Check for refresh_token column
        IF EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'refresh_token'
        ) THEN
            CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_refresh_token
            ON refresh_token_sessions(refresh_token);
            RAISE NOTICE '✅ Created index on refresh_token column';
        ELSE
            RAISE NOTICE '⚠️ Column "refresh_token" does not exist in refresh_token_sessions';
        END IF;

        -- Check for user_id column
        IF EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'user_id'
        ) THEN
            CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_user_id
            ON refresh_token_sessions(user_id);
            RAISE NOTICE '✅ Created index on user_id column';
        END IF;

        -- Check for expires_at column
        IF EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'expires_at'
        ) THEN
            CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_expires_at
            ON refresh_token_sessions(expires_at);
            RAISE NOTICE '✅ Created index on expires_at column';
        END IF;

        -- Check for revoked column
        IF EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'revoked'
        ) THEN
            CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_revoked
            ON refresh_token_sessions(revoked) WHERE revoked = false;
            RAISE NOTICE '✅ Created index on revoked column';
        END IF;

    ELSE
        RAISE NOTICE '⚠️ refresh_token_sessions table does not exist - creating it...';

        -- Create the table if it doesn't exist
        CREATE TABLE IF NOT EXISTS refresh_token_sessions (
            id BIGSERIAL PRIMARY KEY,
            token VARCHAR(255) NOT NULL UNIQUE,
            refresh_token VARCHAR(255) NOT NULL UNIQUE,
            user_id BIGINT NOT NULL,
            expires_at TIMESTAMP NOT NULL,
            revoked BOOLEAN DEFAULT false,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );

        -- Create indexes on the new table
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_token
            ON refresh_token_sessions(token);
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_refresh_token
            ON refresh_token_sessions(refresh_token);
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_user_id
            ON refresh_token_sessions(user_id);
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_expires_at
            ON refresh_token_sessions(expires_at);
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_revoked
            ON refresh_token_sessions(revoked) WHERE revoked = false;

        RAISE NOTICE '✅ Created refresh_token_sessions table and indexes';
    END IF;
END $$;

-- ==========================================
-- PART 4: Table Comments
-- ==========================================

COMMENT ON TABLE blog_posts IS 'Blog posts and articles';
COMMENT ON TABLE blog_media IS 'Media files associated with blog posts';
COMMENT ON TABLE blog_post_tags IS 'Tags for blog posts';
COMMENT ON TABLE hero_sections IS 'Hero sections for different pages and languages';
COMMENT ON TABLE client_logos IS 'Client company logos for social proof';
COMMENT ON TABLE refresh_token_sessions IS 'Refresh token sessions for authentication';
COMMENT ON TABLE users IS 'Application users';
COMMENT ON COLUMN contact_messages.admin_note IS 'Internal admin notes for contact messages';

-- ==========================================
-- PART 5: Verification
-- ==========================================

DO $$
DECLARE
    missing_tables text[];
    table_list text[] := ARRAY[
        'blog_posts', 'blog_media', 'blog_post_tags',
        'hero_sections', 'client_logos', 'users'
    ];
    t text;
BEGIN
    FOREACH t IN ARRAY table_list
    LOOP
        IF NOT EXISTS (SELECT 1 FROM information_schema.tables
                       WHERE table_schema = 'public' AND table_name = t) THEN
            missing_tables := array_append(missing_tables, t);
        END IF;
    END LOOP;

    IF array_length(missing_tables, 1) > 0 THEN
        RAISE NOTICE '⚠️ WARNING: Still missing tables: %', array_to_string(missing_tables, ', ');
    ELSE
        RAISE NOTICE '✅ All required tables exist!';
    END IF;

    -- Verify refresh_token_sessions
    IF EXISTS (SELECT 1 FROM information_schema.tables
               WHERE table_schema = 'public' AND table_name = 'refresh_token_sessions') THEN
        RAISE NOTICE '✅ refresh_token_sessions exists';
    END IF;
END $$;

DO $$
BEGIN
    RAISE NOTICE '✅ Migration V27 completed successfully!';
END $$;