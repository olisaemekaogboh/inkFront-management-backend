-- ==========================================
-- Migration V24: Create Client Logos Table
-- ==========================================
-- Based on JPA entity ClientLogo
-- ==========================================

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

-- Add indexes based on @Index annotations
CREATE INDEX IF NOT EXISTS idx_client_logos_language_status
    ON client_logos(language, status);

CREATE INDEX IF NOT EXISTS idx_client_logos_display_order
    ON client_logos(display_order);

CREATE INDEX IF NOT EXISTS idx_client_logos_featured
    ON client_logos(featured) WHERE featured = true;

-- Add comments
COMMENT ON TABLE client_logos IS 'Client company logos for social proof';