CREATE TABLE testimonials (
    id BIGSERIAL PRIMARY KEY,
    client_name VARCHAR(120) NOT NULL,
    client_role VARCHAR(120),
    organization VARCHAR(120),
    quote TEXT NOT NULL,
    avatar_url VARCHAR(255),
    language VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_testimonials_language ON testimonials(language);
CREATE INDEX idx_testimonials_status ON testimonials(status);
CREATE INDEX idx_testimonials_featured ON testimonials(featured);
CREATE INDEX idx_testimonials_display_order ON testimonials(display_order);