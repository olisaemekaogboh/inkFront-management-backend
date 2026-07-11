-- ==========================================
-- Migration V28: Fix Missing service_interest Column
-- ==========================================
-- V17 is marked successful but the column is missing
-- This adds the column if it doesn't exist
-- ==========================================

-- Add the missing service_interest column
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'service_interest'
    ) THEN
        ALTER TABLE contact_messages
        ADD COLUMN service_interest VARCHAR(150);

        RAISE NOTICE '✅ Added missing service_interest column to contact_messages';
    ELSE
        RAISE NOTICE 'ℹ️ service_interest column already exists';
    END IF;

    -- Also verify other columns from V18 are present
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'preferred_language'
    ) THEN
        ALTER TABLE contact_messages
        ADD COLUMN preferred_language VARCHAR(10) DEFAULT 'EN';
        RAISE NOTICE '✅ Added preferred_language column';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'priority'
    ) THEN
        ALTER TABLE contact_messages
        ADD COLUMN priority VARCHAR(30) DEFAULT 'NORMAL';
        RAISE NOTICE '✅ Added priority column';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'assigned_to'
    ) THEN
        ALTER TABLE contact_messages
        ADD COLUMN assigned_to VARCHAR(150);
        RAISE NOTICE '✅ Added assigned_to column';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'source'
    ) THEN
        ALTER TABLE contact_messages
        ADD COLUMN source VARCHAR(80) DEFAULT 'WEBSITE';
        RAISE NOTICE '✅ Added source column';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'last_contacted_at'
    ) THEN
        ALTER TABLE contact_messages
        ADD COLUMN last_contacted_at TIMESTAMP;
        RAISE NOTICE '✅ Added last_contacted_at column';
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'resolved_at'
    ) THEN
        ALTER TABLE contact_messages
        ADD COLUMN resolved_at TIMESTAMP;
        RAISE NOTICE '✅ Added resolved_at column';
    END IF;
END $$;

-- Verify all columns now exist
DO $$
DECLARE
    missing_columns text[];
    column_list text[] := ARRAY[
        'service_interest', 'preferred_language', 'priority',
        'assigned_to', 'source', 'last_contacted_at', 'resolved_at'
    ];
    col text;
BEGIN
    FOREACH col IN ARRAY column_list
    LOOP
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'contact_messages'
            AND column_name = col
        ) THEN
            missing_columns := array_append(missing_columns, col);
        END IF;
    END LOOP;

    IF array_length(missing_columns, 1) > 0 THEN
        RAISE NOTICE '⚠️ Still missing columns: %', array_to_string(missing_columns, ', ');
    ELSE
        RAISE NOTICE '✅ All contact_messages columns are present!';
    END IF;
END $$;

-- Add comments
COMMENT ON COLUMN contact_messages.service_interest IS 'Service the contact is interested in';
COMMENT ON COLUMN contact_messages.preferred_language IS 'Preferred language for communication';
COMMENT ON COLUMN contact_messages.priority IS 'Priority level: LOW, NORMAL, HIGH, URGENT';
COMMENT ON COLUMN contact_messages.assigned_to IS 'Admin assigned to handle this message';
COMMENT ON COLUMN contact_messages.source IS 'Where the message came from (WEBSITE, API, etc)';
COMMENT ON COLUMN contact_messages.last_contacted_at IS 'Last time this message was contacted';
COMMENT ON COLUMN contact_messages.resolved_at IS 'When this message was resolved';

DO $$
BEGIN
    RAISE NOTICE '✅ Migration V28 completed successfully!';
END $$;