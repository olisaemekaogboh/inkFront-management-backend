-- ==========================================
-- Migration V36: Add last_used_at to Refresh Token Sessions
-- ==========================================
-- Adds only the missing last_used_at column
-- ==========================================

ALTER TABLE refresh_token_sessions ADD COLUMN IF NOT EXISTS last_used_at TIMESTAMP;

-- Verify column was added
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'refresh_token_sessions'
        AND column_name = 'last_used_at'
    ) THEN
        RAISE NOTICE '✅ last_used_at column exists';
    END IF;
END $$;