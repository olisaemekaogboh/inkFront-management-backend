ALTER TABLE service_items DROP CONSTRAINT IF EXISTS uk_service_items_slug;
ALTER TABLE service_items DROP CONSTRAINT IF EXISTS service_items_slug_key;

ALTER TABLE service_items
ADD CONSTRAINT uk_service_items_slug_language UNIQUE (slug, language);