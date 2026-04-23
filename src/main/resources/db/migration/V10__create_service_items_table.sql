CREATE TABLE service_items (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(180) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    short_description VARCHAR(200) NOT NULL,
    full_description TEXT,
    category VARCHAR(100),
    icon_key VARCHAR(100),
    language VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    display_order INTEGER NOT NULL DEFAULT 0,
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_service_items_slug ON service_items(slug);
CREATE INDEX idx_service_items_language ON service_items(language);
CREATE INDEX idx_service_items_status ON service_items(status);
CREATE INDEX idx_service_items_display_order ON service_items(display_order);
CREATE INDEX idx_service_items_featured ON service_items(featured);
CREATE INDEX idx_service_items_category ON service_items(category);