-- ==========================================
-- Migration V34: Add Missing Columns to Product Blueprints
-- ==========================================
-- Adds all columns that might be missing from product_blueprints
-- Based on the JPA entity
-- ==========================================

DO $$
BEGIN
    -- Add image_url (the current error)
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'product_blueprints' AND column_name = 'image_url'
    ) THEN
        ALTER TABLE product_blueprints ADD COLUMN image_url VARCHAR(500);
        RAISE NOTICE '✅ Added image_url to product_blueprints';
    END IF;

    -- Add description (in case it's still missing)
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'product_blueprints' AND column_name = 'description'
    ) THEN
        ALTER TABLE product_blueprints ADD COLUMN description TEXT;
        RAISE NOTICE '✅ Added description to product_blueprints';
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

    RAISE NOTICE '✅ All product_blueprints columns verified';
END $$;

-- Clean up: Delete failed migrations so they don't block future ones
DO $$
BEGIN
    -- This is just informational - we handle failures separately
    RAISE NOTICE 'ℹ️ If V32 or V33 failed, delete them manually:';
    RAISE NOTICE '   DELETE FROM flyway_schema_history WHERE version IN (''32'', ''33'') AND success = false;';
END $$;

DO $$
BEGIN
    RAISE NOTICE '✅ Migration V34 completed successfully!';
END $$;