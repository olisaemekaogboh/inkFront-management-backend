-- ==========================================
-- Migration V37: Complete Refresh Token Sessions Migration
-- ==========================================
-- Adds ALL missing columns based on the RefreshTokenSession entity
-- ==========================================

DO $$
BEGIN
    RAISE NOTICE 'Starting refresh_token_sessions column check...';

    -- Check if table exists
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.tables
        WHERE table_schema = 'public'
        AND table_name = 'refresh_token_sessions'
    ) THEN
        RAISE NOTICE '⚠️ refresh_token_sessions table does not exist - creating it...';

        CREATE TABLE refresh_token_sessions (
            id BIGSERIAL PRIMARY KEY,
            user_id BIGINT NOT NULL,
            token_id VARCHAR(120) UNIQUE,
            replaced_by_token_id VARCHAR(120),
            token_hash VARCHAR(255) UNIQUE,
            expires_at TIMESTAMP NOT NULL,
            revoked BOOLEAN NOT NULL DEFAULT false,
            revoked_at TIMESTAMP,
            last_used_at TIMESTAMP,
            ip_address VARCHAR(80),
            user_agent VARCHAR(700),
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );

        RAISE NOTICE '✅ Created refresh_token_sessions table with all columns';
        RETURN;
    END IF;

    -- Add token_id column
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'refresh_token_sessions'
        AND column_name = 'token_id'
    ) THEN
        ALTER TABLE refresh_token_sessions ADD COLUMN token_id VARCHAR(120) UNIQUE;
        RAISE NOTICE '✅ Added token_id column';
    END IF;

    -- Add replaced_by_token_id column
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'refresh_token_sessions'
        AND column_name = 'replaced_by_token_id'
    ) THEN
        ALTER TABLE refresh_token_sessions ADD COLUMN replaced_by_token_id VARCHAR(120);
        RAISE NOTICE '✅ Added replaced_by_token_id column';
    END IF;

    -- Add token_hash column
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'refresh_token_sessions'
        AND column_name = 'token_hash'
    ) THEN
        ALTER TABLE refresh_token_sessions ADD COLUMN token_hash VARCHAR(255) UNIQUE;
        RAISE NOTICE '✅ Added token_hash column';
    END IF;

    -- Add revoked column (if missing)
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'refresh_token_sessions'
        AND column_name = 'revoked'
    ) THEN
        ALTER TABLE refresh_token_sessions ADD COLUMN revoked BOOLEAN NOT NULL DEFAULT false;
        RAISE NOTICE '✅ Added revoked column';
    END IF;

    -- Add revoked_at column
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'refresh_token_sessions'
        AND column_name = 'revoked_at'
    ) THEN
        ALTER TABLE refresh_token_sessions ADD COLUMN revoked_at TIMESTAMP;
        RAISE NOTICE '✅ Added revoked_at column';
    END IF;

    -- Add last_used_at column
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'refresh_token_sessions'
        AND column_name = 'last_used_at'
    ) THEN
        ALTER TABLE refresh_token_sessions ADD COLUMN last_used_at TIMESTAMP;
        RAISE NOTICE '✅ Added last_used_at column';
    END IF;

    -- Add ip_address column
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'refresh_token_sessions'
        AND column_name = 'ip_address'
    ) THEN
        ALTER TABLE refresh_token_sessions ADD COLUMN ip_address VARCHAR(80);
        RAISE NOTICE '✅ Added ip_address column';
    END IF;

    -- Add user_agent column
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'refresh_token_sessions'
        AND column_name = 'user_agent'
    ) THEN
        ALTER TABLE refresh_token_sessions ADD COLUMN user_agent VARCHAR(700);
        RAISE NOTICE '✅ Added user_agent column';
    END IF;

    RAISE NOTICE '✅ All refresh_token_sessions columns verified';
END $$;

-- Create indexes
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'refresh_token_sessions') THEN
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_user_id ON refresh_token_sessions(user_id);
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_token_id ON refresh_token_sessions(token_id);
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_expires_at ON refresh_token_sessions(expires_at);
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_revoked_at ON refresh_token_sessions(revoked_at);
        RAISE NOTICE '✅ Created indexes';
    END IF;
END $$;

-- Add table comment
COMMENT ON TABLE refresh_token_sessions IS 'Refresh token sessions for JWT authentication';
COMMENT ON COLUMN refresh_token_sessions.token_id IS 'Unique token identifier';
COMMENT ON COLUMN refresh_token_sessions.replaced_by_token_id IS 'Token ID that replaced this one';
COMMENT ON COLUMN refresh_token_sessions.token_hash IS 'Hashed token value';
COMMENT ON COLUMN refresh_token_sessions.revoked IS 'Whether the token is revoked';
COMMENT ON COLUMN refresh_token_sessions.revoked_at IS 'When the token was revoked';
COMMENT ON COLUMN refresh_token_sessions.last_used_at IS 'When the token was last used';
COMMENT ON COLUMN refresh_token_sessions.ip_address IS 'IP address that used this token';
COMMENT ON COLUMN refresh_token_sessions.user_agent IS 'User agent that used this token';

DO $$
BEGIN
    RAISE NOTICE '✅ Migration V37 completed successfully!';
END $$;