-- ==========================================
-- Migration V35: Add Missing Columns to Project Items
-- ==========================================
-- Adds all columns that might be missing from project_items
-- ==========================================

DO $$
BEGIN
    -- Add client_name (the current error)
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'client_name'
    ) THEN
        ALTER TABLE project_items ADD COLUMN client_name VARCHAR(255);
        RAISE NOTICE '✅ Added client_name to project_items';
    END IF;

    -- Add client_industry if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'client_industry'
    ) THEN
        ALTER TABLE project_items ADD COLUMN client_industry VARCHAR(100);
        RAISE NOTICE '✅ Added client_industry to project_items';
    END IF;

    -- Add project_type if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'project_type'
    ) THEN
        ALTER TABLE project_items ADD COLUMN project_type VARCHAR(100);
        RAISE NOTICE '✅ Added project_type to project_items';
    END IF;

    -- Add cover_image_url if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'cover_image_url'
    ) THEN
        ALTER TABLE project_items ADD COLUMN cover_image_url VARCHAR(500);
        RAISE NOTICE '✅ Added cover_image_url to project_items';
    END IF;

    -- Add live_url if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'live_url'
    ) THEN
        ALTER TABLE project_items ADD COLUMN live_url VARCHAR(255);
        RAISE NOTICE '✅ Added live_url to project_items';
    END IF;

    -- Add featured if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'featured'
    ) THEN
        ALTER TABLE project_items ADD COLUMN featured BOOLEAN DEFAULT false;
        RAISE NOTICE '✅ Added featured to project_items';
    END IF;

    -- Add display_order if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'display_order'
    ) THEN
        ALTER TABLE project_items ADD COLUMN display_order INTEGER DEFAULT 0;
        RAISE NOTICE '✅ Added display_order to project_items';
    END IF;

    -- Add language if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'language'
    ) THEN
        ALTER TABLE project_items ADD COLUMN language VARCHAR(20) DEFAULT 'EN';
        RAISE NOTICE '✅ Added language to project_items';
    END IF;

    -- Add status if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'status'
    ) THEN
        ALTER TABLE project_items ADD COLUMN status VARCHAR(20) DEFAULT 'DRAFT';
        RAISE NOTICE '✅ Added status to project_items';
    END IF;

    -- Add created_at if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'created_at'
    ) THEN
        ALTER TABLE project_items ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
        RAISE NOTICE '✅ Added created_at to project_items';
    END IF;

    -- Add updated_at if missing
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'updated_at'
    ) THEN
        ALTER TABLE project_items ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
        RAISE NOTICE '✅ Added updated_at to project_items';
    END IF;

    RAISE NOTICE '✅ All project_items columns verified';
END $$;

DO $$
BEGIN
    RAISE NOTICE '✅ Migration V35 completed successfully!';
END $$;