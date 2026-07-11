-- ==========================================
-- Migration V36: Add image_url to Project Items
-- ==========================================
-- Adds image_url column to project_items
-- ==========================================

DO $$
BEGIN
    -- Add image_url (the current error)
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'image_url'
    ) THEN
        ALTER TABLE project_items ADD COLUMN image_url VARCHAR(500);
        RAISE NOTICE '✅ Added image_url to project_items';
    ELSE
        RAISE NOTICE 'ℹ️ image_url already exists in project_items';
    END IF;

    -- Also check if there are any other missing columns
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'client_name'
    ) THEN
        ALTER TABLE project_items ADD COLUMN client_name VARCHAR(255);
        RAISE NOTICE '✅ Added client_name to project_items';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'project_items' AND column_name = 'description'
    ) THEN
        ALTER TABLE project_items ADD COLUMN description TEXT;
        RAISE NOTICE '✅ Added description to project_items';
    END IF;

    RAISE NOTICE '✅ All project_items columns verified';
END $$;

DO $$
BEGIN
    RAISE NOTICE '✅ Migration V36 completed successfully!';
END $$;