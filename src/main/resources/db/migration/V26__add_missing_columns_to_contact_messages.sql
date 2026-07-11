-- ==========================================
-- Migration V22: Add Missing Columns to Contact Messages
-- ==========================================
-- Adds the admin_note column to contact_messages
-- Based on JPA entity expectations
-- ==========================================

-- Check if admin_note column exists, add if missing
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'admin_note'
    ) THEN
        ALTER TABLE contact_messages ADD COLUMN admin_note TEXT;
        RAISE NOTICE 'Added column: admin_note to contact_messages';
    ELSE
        RAISE NOTICE 'Column admin_note already exists in contact_messages';
    END IF;

    -- Also check for any other missing columns that might be needed
    -- Based on common JPA entity patterns for contact messages

    -- Check for responded_at
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'responded_at'
    ) THEN
        ALTER TABLE contact_messages ADD COLUMN responded_at TIMESTAMP;
        RAISE NOTICE 'Added column: responded_at to contact_messages';
    END IF;

    -- Check for responded_by
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'responded_by'
    ) THEN
        ALTER TABLE contact_messages ADD COLUMN responded_by VARCHAR(100);
        RAISE NOTICE 'Added column: responded_by to contact_messages';
    END IF;

    -- Check for read_at
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'read_at'
    ) THEN
        ALTER TABLE contact_messages ADD COLUMN read_at TIMESTAMP;
        RAISE NOTICE 'Added column: read_at to contact_messages';
    END IF;

    -- Check for is_read
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'is_read'
    ) THEN
        ALTER TABLE contact_messages ADD COLUMN is_read BOOLEAN DEFAULT false;
        RAISE NOTICE 'Added column: is_read to contact_messages';
    END IF;

    -- Check for is_resolved
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'is_resolved'
    ) THEN
        ALTER TABLE contact_messages ADD COLUMN is_resolved BOOLEAN DEFAULT false;
        RAISE NOTICE 'Added column: is_resolved to contact_messages';
    END IF;

    -- Check for resolved_at
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'resolved_at'
    ) THEN
        ALTER TABLE contact_messages ADD COLUMN resolved_at TIMESTAMP;
        RAISE NOTICE 'Added column: resolved_at to contact_messages';
    END IF;

    -- Check for priority
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'priority'
    ) THEN
        ALTER TABLE contact_messages ADD COLUMN priority VARCHAR(20) DEFAULT 'NORMAL';
        RAISE NOTICE 'Added column: priority to contact_messages';
    END IF;

    -- Check for subject (if missing)
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'contact_messages'
        AND column_name = 'subject'
    ) THEN
        ALTER TABLE contact_messages ADD COLUMN subject VARCHAR(255);
        RAISE NOTICE 'Added column: subject to contact_messages';
    END IF;
END $$;

-- Add comments for the new columns
COMMENT ON COLUMN contact_messages.admin_note IS 'Internal admin notes about this message';
COMMENT ON COLUMN contact_messages.responded_at IS 'When the message was last responded to';
COMMENT ON COLUMN contact_messages.responded_by IS 'Who responded to the message';
COMMENT ON COLUMN contact_messages.read_at IS 'When the message was first read by admin';
COMMENT ON COLUMN contact_messages.is_read IS 'Whether the message has been read';
COMMENT ON COLUMN contact_messages.is_resolved IS 'Whether the message has been resolved';
COMMENT ON COLUMN contact_messages.resolved_at IS 'When the message was resolved';
COMMENT ON COLUMN contact_messages.priority IS 'Priority level: LOW, NORMAL, HIGH, URGENT';
COMMENT ON COLUMN contact_messages.subject IS 'Message subject line';

DO $$
BEGIN
    RAISE NOTICE 'Migration V22 completed: Added missing columns to contact_messages';
END $$;