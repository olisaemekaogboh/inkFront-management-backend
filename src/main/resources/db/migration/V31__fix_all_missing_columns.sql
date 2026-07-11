-- ==========================================
-- Migration V31: Fix All Missing Columns
-- ==========================================
-- Adds missing columns to all tables
-- Based on JPA entity expectations
-- ==========================================

-- 1. Fix homepage_sections
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'homepage_sections' AND column_name = 'featured'
    ) THEN
        ALTER TABLE homepage_sections ADD COLUMN featured BOOLEAN DEFAULT false;
        RAISE NOTICE '✅ Added featured to homepage_sections';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'homepage_sections' AND column_name = 'icon'
    ) THEN
        ALTER TABLE homepage_sections ADD COLUMN icon VARCHAR(100);
        RAISE NOTICE '✅ Added icon to homepage_sections';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'homepage_sections' AND column_name = 'image_url'
    ) THEN
        ALTER TABLE homepage_sections ADD COLUMN image_url VARCHAR(500);
        RAISE NOTICE '✅ Added image_url to homepage_sections';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'homepage_sections' AND column_name = 'cta_label'
    ) THEN
        ALTER TABLE homepage_sections ADD COLUMN cta_label VARCHAR(100);
        RAISE NOTICE '✅ Added cta_label to homepage_sections';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'homepage_sections' AND column_name = 'cta_url'
    ) THEN
        ALTER TABLE homepage_sections ADD COLUMN cta_url VARCHAR(255);
        RAISE NOTICE '✅ Added cta_url to homepage_sections';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'homepage_sections' AND column_name = 'background_color'
    ) THEN
        ALTER TABLE homepage_sections ADD COLUMN background_color VARCHAR(50);
        RAISE NOTICE '✅ Added background_color to homepage_sections';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'homepage_sections' AND column_name = 'text_color'
    ) THEN
        ALTER TABLE homepage_sections ADD COLUMN text_color VARCHAR(50);
        RAISE NOTICE '✅ Added text_color to homepage_sections';
    END IF;
END $$;

-- 2. Check other tables for missing columns
-- Add any other common missing columns

-- Project items might need featured
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'project_items') THEN
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'project_items' AND column_name = 'featured'
        ) THEN
            ALTER TABLE project_items ADD COLUMN featured BOOLEAN DEFAULT false;
            RAISE NOTICE '✅ Added featured to project_items';
        END IF;
    END IF;
END $$;

-- Testimonials might need featured
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'testimonials') THEN
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'testimonials' AND column_name = 'featured'
        ) THEN
            ALTER TABLE testimonials ADD COLUMN featured BOOLEAN DEFAULT false;
            RAISE NOTICE '✅ Added featured to testimonials';
        END IF;
    END IF;
END $$;

-- Product blueprints might need featured
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'product_blueprints') THEN
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'product_blueprints' AND column_name = 'featured'
        ) THEN
            ALTER TABLE product_blueprints ADD COLUMN featured BOOLEAN DEFAULT false;
            RAISE NOTICE '✅ Added featured to product_blueprints';
        END IF;
    END IF;
END $$;

-- Create indexes
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'homepage_sections') THEN
        CREATE INDEX IF NOT EXISTS idx_homepage_sections_featured ON homepage_sections(featured) WHERE featured = true;
        CREATE INDEX IF NOT EXISTS idx_homepage_sections_display_order ON homepage_sections(display_order);
        CREATE INDEX IF NOT EXISTS idx_homepage_sections_status ON homepage_sections(status);
        CREATE INDEX IF NOT EXISTS idx_homepage_sections_language ON homepage_sections(language);
    END IF;
END $$;

DO $$
BEGIN
    RAISE NOTICE '✅ Migration V31 completed successfully!';
END $$;