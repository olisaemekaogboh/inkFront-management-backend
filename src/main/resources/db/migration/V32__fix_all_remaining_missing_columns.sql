-- ==========================================
-- Migration V32: Fix All Remaining Missing Columns
-- ==========================================
-- Adds missing columns to all tables based on JPA entities
-- Comprehensive fix for all potential issues
-- ==========================================

-- ==========================================
-- 1. Fix product_blueprints table
-- ==========================================
DO $$
BEGIN
    -- Add description column (the current error)
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'product_blueprints' AND column_name = 'description'
    ) THEN
        ALTER TABLE product_blueprints ADD COLUMN description TEXT;
        RAISE NOTICE '✅ Added description to product_blueprints';
    END IF;

    -- Add featured if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'product_blueprints' AND column_name = 'featured'
    ) THEN
        ALTER TABLE product_blueprints ADD COLUMN featured BOOLEAN DEFAULT false;
        RAISE NOTICE '✅ Added featured to product_blueprints';
    END IF;

    -- Add display_order if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'product_blueprints' AND column_name = 'display_order'
    ) THEN
        ALTER TABLE product_blueprints ADD COLUMN display_order INTEGER DEFAULT 0;
        RAISE NOTICE '✅ Added display_order to product_blueprints';
    END IF;

    -- Add language if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'product_blueprints' AND column_name = 'language'
    ) THEN
        ALTER TABLE product_blueprints ADD COLUMN language VARCHAR(20) DEFAULT 'EN';
        RAISE NOTICE '✅ Added language to product_blueprints';
    END IF;

    -- Add status if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'product_blueprints' AND column_name = 'status'
    ) THEN
        ALTER TABLE product_blueprints ADD COLUMN status VARCHAR(20) DEFAULT 'DRAFT';
        RAISE NOTICE '✅ Added status to product_blueprints';
    END IF;

    -- Add created_at if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'product_blueprints' AND column_name = 'created_at'
    ) THEN
        ALTER TABLE product_blueprints ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
        RAISE NOTICE '✅ Added created_at to product_blueprints';
    END IF;

    -- Add updated_at if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'product_blueprints' AND column_name = 'updated_at'
    ) THEN
        ALTER TABLE product_blueprints ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
        RAISE NOTICE '✅ Added updated_at to product_blueprints';
    END IF;

    -- Add hero_image_url if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'product_blueprints' AND column_name = 'hero_image_url'
    ) THEN
        ALTER TABLE product_blueprints ADD COLUMN hero_image_url VARCHAR(500);
        RAISE NOTICE '✅ Added hero_image_url to product_blueprints';
    END IF;

    -- Add challenge_statement if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'product_blueprints' AND column_name = 'challenge_statement'
    ) THEN
        ALTER TABLE product_blueprints ADD COLUMN challenge_statement TEXT;
        RAISE NOTICE '✅ Added challenge_statement to product_blueprints';
    END IF;

    -- Add solution_overview if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'product_blueprints' AND column_name = 'solution_overview'
    ) THEN
        ALTER TABLE product_blueprints ADD COLUMN solution_overview TEXT;
        RAISE NOTICE '✅ Added solution_overview to product_blueprints';
    END IF;

    -- Add feature_highlights if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'product_blueprints' AND column_name = 'feature_highlights'
    ) THEN
        ALTER TABLE product_blueprints ADD COLUMN feature_highlights TEXT;
        RAISE NOTICE '✅ Added feature_highlights to product_blueprints';
    END IF;
END $$;

-- ==========================================
-- 2. Fix project_items table
-- ==========================================
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

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'project_items' AND column_name = 'display_order'
        ) THEN
            ALTER TABLE project_items ADD COLUMN display_order INTEGER DEFAULT 0;
            RAISE NOTICE '✅ Added display_order to project_items';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'project_items' AND column_name = 'language'
        ) THEN
            ALTER TABLE project_items ADD COLUMN language VARCHAR(20) DEFAULT 'EN';
            RAISE NOTICE '✅ Added language to project_items';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'project_items' AND column_name = 'status'
        ) THEN
            ALTER TABLE project_items ADD COLUMN status VARCHAR(20) DEFAULT 'DRAFT';
            RAISE NOTICE '✅ Added status to project_items';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'project_items' AND column_name = 'created_at'
        ) THEN
            ALTER TABLE project_items ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
            RAISE NOTICE '✅ Added created_at to project_items';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'project_items' AND column_name = 'updated_at'
        ) THEN
            ALTER TABLE project_items ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
            RAISE NOTICE '✅ Added updated_at to project_items';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'project_items' AND column_name = 'cover_image_url'
        ) THEN
            ALTER TABLE project_items ADD COLUMN cover_image_url VARCHAR(500);
            RAISE NOTICE '✅ Added cover_image_url to project_items';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'project_items' AND column_name = 'live_url'
        ) THEN
            ALTER TABLE project_items ADD COLUMN live_url VARCHAR(255);
            RAISE NOTICE '✅ Added live_url to project_items';
        END IF;
    END IF;
END $$;

-- ==========================================
-- 3. Fix testimonials table
-- ==========================================
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

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'testimonials' AND column_name = 'display_order'
        ) THEN
            ALTER TABLE testimonials ADD COLUMN display_order INTEGER DEFAULT 0;
            RAISE NOTICE '✅ Added display_order to testimonials';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'testimonials' AND column_name = 'language'
        ) THEN
            ALTER TABLE testimonials ADD COLUMN language VARCHAR(20) DEFAULT 'EN';
            RAISE NOTICE '✅ Added language to testimonials';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'testimonials' AND column_name = 'status'
        ) THEN
            ALTER TABLE testimonials ADD COLUMN status VARCHAR(20) DEFAULT 'DRAFT';
            RAISE NOTICE '✅ Added status to testimonials';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'testimonials' AND column_name = 'created_at'
        ) THEN
            ALTER TABLE testimonials ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
            RAISE NOTICE '✅ Added created_at to testimonials';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'testimonials' AND column_name = 'updated_at'
        ) THEN
            ALTER TABLE testimonials ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
            RAISE NOTICE '✅ Added updated_at to testimonials';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'testimonials' AND column_name = 'avatar_url'
        ) THEN
            ALTER TABLE testimonials ADD COLUMN avatar_url VARCHAR(500);
            RAISE NOTICE '✅ Added avatar_url to testimonials';
        END IF;
    END IF;
END $$;

-- ==========================================
-- 4. Fix service_items table (additional columns)
-- ==========================================
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'service_items') THEN
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'service_items' AND column_name = 'created_at'
        ) THEN
            ALTER TABLE service_items ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
            RAISE NOTICE '✅ Added created_at to service_items';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'service_items' AND column_name = 'updated_at'
        ) THEN
            ALTER TABLE service_items ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
            RAISE NOTICE '✅ Added updated_at to service_items';
        END IF;
    END IF;
END $$;

-- ==========================================
-- 5. Fix hero_sections table (additional columns)
-- ==========================================
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'hero_sections') THEN
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'hero_sections' AND column_name = 'created_at'
        ) THEN
            ALTER TABLE hero_sections ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
            RAISE NOTICE '✅ Added created_at to hero_sections';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'hero_sections' AND column_name = 'updated_at'
        ) THEN
            ALTER TABLE hero_sections ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
            RAISE NOTICE '✅ Added updated_at to hero_sections';
        END IF;
    END IF;
END $$;

-- ==========================================
-- 6. Fix client_logos table (additional columns)
-- ==========================================
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'client_logos') THEN
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'client_logos' AND column_name = 'created_at'
        ) THEN
            ALTER TABLE client_logos ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
            RAISE NOTICE '✅ Added created_at to client_logos';
        END IF;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'client_logos' AND column_name = 'updated_at'
        ) THEN
            ALTER TABLE client_logos ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
            RAISE NOTICE '✅ Added updated_at to client_logos';
        END IF;
    END IF;
END $$;

-- ==========================================
-- 7. Create indexes for product_blueprints
-- ==========================================
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'product_blueprints') THEN
        CREATE INDEX IF NOT EXISTS idx_product_blueprints_slug ON product_blueprints(slug);
        CREATE INDEX IF NOT EXISTS idx_product_blueprints_featured ON product_blueprints(featured) WHERE featured = true;
        CREATE INDEX IF NOT EXISTS idx_product_blueprints_display_order ON product_blueprints(display_order);
        CREATE INDEX IF NOT EXISTS idx_product_blueprints_status ON product_blueprints(status);
        CREATE INDEX IF NOT EXISTS idx_product_blueprints_language ON product_blueprints(language);
        RAISE NOTICE '✅ Created indexes for product_blueprints';
    END IF;
END $$;

-- ==========================================
-- 8. Verification
-- ==========================================
DO $$
DECLARE
    missing_columns text[];
    table_checks text[] := ARRAY[
        'product_blueprints.description',
        'product_blueprints.featured',
        'product_blueprints.display_order',
        'project_items.featured',
        'project_items.display_order',
        'testimonials.featured',
        'testimonials.display_order'
    ];
    check_item text;
    table_name text;
    column_name text;
BEGIN
    FOREACH check_item IN ARRAY table_checks
    LOOP
        table_name := split_part(check_item, '.', 1);
        column_name := split_part(check_item, '.', 2);

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = table_name AND column_name = column_name
        ) THEN
            missing_columns := array_append(missing_columns, check_item);
        END IF;
    END LOOP;

    IF array_length(missing_columns, 1) > 0 THEN
        RAISE NOTICE '⚠️ Still missing columns: %', array_to_string(missing_columns, ', ');
    ELSE
        RAISE NOTICE '✅ All checked columns are present!';
    END IF;
END $$;

DO $$
BEGIN
    RAISE NOTICE '✅ Migration V32 completed successfully!';
END $$;