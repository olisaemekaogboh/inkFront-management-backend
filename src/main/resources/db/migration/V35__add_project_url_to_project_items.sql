-- ==========================================
-- Migration V37: Add project_url to Project Items
-- ==========================================
-- Adds project_url column to project_items
-- ==========================================

DO $$
BEGIN
    -- Add project_url (the current error)
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'project_url'
    ) THEN
        ALTER TABLE project_items ADD COLUMN project_url VARCHAR(500);
        RAISE NOTICE '✅ Added project_url to project_items';
    ELSE
        RAISE NOTICE 'ℹ️ project_url already exists in project_items';
    END IF;

    -- Check for any other missing columns that might be needed
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'github_url'
    ) THEN
        ALTER TABLE project_items ADD COLUMN github_url VARCHAR(500);
        RAISE NOTICE '✅ Added github_url to project_items';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'technologies'
    ) THEN
        ALTER TABLE project_items ADD COLUMN technologies TEXT;
        RAISE NOTICE '✅ Added technologies to project_items';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'completion_date'
    ) THEN
        ALTER TABLE project_items ADD COLUMN completion_date DATE;
        RAISE NOTICE '✅ Added completion_date to project_items';
    END IF;

    RAISE NOTICE '✅ All project_items columns verified';
END $$;

DO $$
BEGIN
    RAISE NOTICE '✅ Migration V37 completed successfully!';
END $$;