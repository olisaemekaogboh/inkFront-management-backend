CREATE TABLE product_blueprints (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(220) NOT NULL UNIQUE,
    title VARCHAR(180) NOT NULL,
    summary VARCHAR(240) NOT NULL,
    challenge_statement TEXT,
    solution_overview TEXT,
    feature_highlights TEXT,
    hero_image_url VARCHAR(255),
    language VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_product_blueprints_language ON product_blueprints(language);
CREATE INDEX idx_product_blueprints_status ON product_blueprints(status);
CREATE INDEX idx_product_blueprints_featured ON product_blueprints(featured);
CREATE INDEX idx_product_blueprints_display_order ON product_blueprints(display_order);
CREATE INDEX idx_product_blueprints_slug ON product_blueprints(slug);