CREATE TABLE homepage_sections (
    id BIGSERIAL PRIMARY KEY,
    section_key VARCHAR(100) NOT NULL,
    title VARCHAR(150) NOT NULL,
    subtitle TEXT,
    body TEXT,
    language VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_homepage_sections_section_key ON homepage_sections(section_key);
CREATE INDEX idx_homepage_sections_language ON homepage_sections(language);
CREATE INDEX idx_homepage_sections_status ON homepage_sections(status);
CREATE INDEX idx_homepage_sections_display_order ON homepage_sections(display_order);