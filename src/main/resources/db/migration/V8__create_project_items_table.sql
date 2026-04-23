CREATE TABLE project_items (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(180) NOT NULL UNIQUE,
    title VARCHAR(160) NOT NULL,
    summary VARCHAR(220) NOT NULL,
    description TEXT,
    client_industry VARCHAR(120),
    project_type VARCHAR(120),
    cover_image_url VARCHAR(255),
    live_url VARCHAR(255),
    language VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_project_items_slug ON project_items(slug);
CREATE INDEX idx_project_items_language ON project_items(language);
CREATE INDEX idx_project_items_status ON project_items(status);
CREATE INDEX idx_project_items_featured ON project_items(featured);
CREATE INDEX idx_project_items_display_order ON project_items(display_order);