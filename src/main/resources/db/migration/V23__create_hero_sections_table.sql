-- ==========================================
-- Migration V23: Create Hero Sections Table
-- ==========================================
-- Based on JPA entity HeroSection
-- ==========================================

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

-- Add indexes based on @Index annotations
CREATE INDEX IF NOT EXISTS idx_hero_lang_placement_status
    ON hero_sections(language, placement, status);

CREATE INDEX IF NOT EXISTS idx_hero_display_order
    ON hero_sections(display_order);

CREATE INDEX IF NOT EXISTS idx_hero_sections_featured
    ON hero_sections(featured) WHERE featured = true;

-- Add comments
COMMENT ON TABLE hero_sections IS 'Hero sections for different pages and languages';
COMMENT ON COLUMN hero_sections.placement IS 'Page placement: HOME, ABOUT, SERVICES, etc.';