-- ==========================================
-- Migration V28: Add Missing Contact Message Columns
-- ==========================================
-- Adds company and other missing columns to contact_messages
-- ==========================================

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'public'
        AND table_name = 'contact_messages'
    ) THEN
        -- Add company column
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'contact_messages'
            AND column_name = 'company'
        ) THEN
            ALTER TABLE contact_messages ADD COLUMN company VARCHAR(255);
            RAISE NOTICE 'Added company column';
        END IF;

        -- Add admin_note if missing
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'contact_messages'
            AND column_name = 'admin_note'
        ) THEN
            ALTER TABLE contact_messages ADD COLUMN admin_note TEXT;
            RAISE NOTICE 'Added admin_note column';
        END IF;

        -- Add subject if missing
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'contact_messages'
            AND column_name = 'subject'
        ) THEN
            ALTER TABLE contact_messages ADD COLUMN subject VARCHAR(255);
            RAISE NOTICE 'Added subject column';
        END IF;

        -- Add is_read if missing
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'contact_messages'
            AND column_name = 'is_read'
        ) THEN
            ALTER TABLE contact_messages ADD COLUMN is_read BOOLEAN DEFAULT false;
            RAISE NOTICE 'Added is_read column';
        END IF;

        -- Add phone if missing
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'contact_messages'
            AND column_name = 'phone'
        ) THEN
            ALTER TABLE contact_messages ADD COLUMN phone VARCHAR(50);
            RAISE NOTICE 'Added phone column';
        END IF;

        -- Add priority if missing
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name = 'contact_messages'
            AND column_name = 'priority'
        ) THEN
            ALTER TABLE contact_messages ADD COLUMN priority VARCHAR(20) DEFAULT 'NORMAL';
            RAISE NOTICE 'Added priority column';
        END IF;

        RAISE NOTICE '✅ All contact_messages columns verified';
    ELSE
        -- Create the table if it doesn't exist
        CREATE TABLE IF NOT EXISTS contact_messages (
            id BIGSERIAL PRIMARY KEY,
            name VARCHAR(100) NOT NULL,
            email VARCHAR(255) NOT NULL,
            company VARCHAR(255),
            phone VARCHAR(50),
            subject VARCHAR(255),
            message TEXT NOT NULL,
            admin_note TEXT,
            status VARCHAR(20) DEFAULT 'NEW',
            is_read BOOLEAN DEFAULT false,
            is_resolved BOOLEAN DEFAULT false,
            priority VARCHAR(20) DEFAULT 'NORMAL',
            responded_at TIMESTAMP,
            responded_by VARCHAR(100),
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );
        RAISE NOTICE '✅ Created contact_messages table';
    END IF;
END $$;

-- Add indexes
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables
               WHERE table_name = 'contact_messages') THEN
        CREATE INDEX IF NOT EXISTS idx_contact_messages_email ON contact_messages(email);
        CREATE INDEX IF NOT EXISTS idx_contact_messages_created_at ON contact_messages(created_at);
        CREATE INDEX IF NOT EXISTS idx_contact_messages_is_read ON contact_messages(is_read) WHERE is_read = false;
        CREATE INDEX IF NOT EXISTS idx_contact_messages_status ON contact_messages(status);
        CREATE INDEX IF NOT EXISTS idx_contact_messages_priority ON contact_messages(priority);
        RAISE NOTICE '✅ Created indexes for contact_messages';
    END IF;
END $$;

-- Add comments
COMMENT ON COLUMN contact_messages.company IS 'Company name of the contact person';
COMMENT ON COLUMN contact_messages.admin_note IS 'Internal admin notes about this message';
COMMENT ON COLUMN contact_messages.priority IS 'Priority level: LOW, NORMAL, HIGH, URGENT';
COMMENT ON COLUMN contact_messages.is_read IS 'Whether an admin has read this message';
COMMENT ON COLUMN contact_messages.is_resolved IS 'Whether this message has been resolved';

DO $$
BEGIN
    RAISE NOTICE '✅ Migration V28 completed successfully!';
END $$;