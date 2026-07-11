-- ==========================================
-- Migration V38: Add Missing Columns to Refresh Token Sessions
-- ==========================================
-- Adds last_used_at and other missing columns to refresh_token_sessions
-- ==========================================

DO $$
BEGIN
    -- Check if refresh_token_sessions table exists
    IF EXISTS (
        SELECT 1 FROM information_schema.tables
        WHERE table_schema = 'public'
        AND table_name = 'refresh_token_sessions'
    ) THEN
        RAISE NOTICE '✅ refresh_token_sessions table exists, checking columns...';

        -- Add last_used_at (the current error)
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'last_used_at'
        ) THEN
            ALTER TABLE refresh_token_sessions ADD COLUMN last_used_at TIMESTAMP;
            RAISE NOTICE '✅ Added last_used_at to refresh_token_sessions';
        END IF;

        -- Add user_agent if missing
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'user_agent'
        ) THEN
            ALTER TABLE refresh_token_sessions ADD COLUMN user_agent VARCHAR(255);
            RAISE NOTICE '✅ Added user_agent to refresh_token_sessions';
        END IF;

        -- Add ip_address if missing
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'ip_address'
        ) THEN
            ALTER TABLE refresh_token_sessions ADD COLUMN ip_address VARCHAR(45);
            RAISE NOTICE '✅ Added ip_address to refresh_token_sessions';
        END IF;

        -- Add device_name if missing
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'device_name'
        ) THEN
            ALTER TABLE refresh_token_sessions ADD COLUMN device_name VARCHAR(100);
            RAISE NOTICE '✅ Added device_name to refresh_token_sessions';
        END IF;

        -- Add browser_name if missing
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'browser_name'
        ) THEN
            ALTER TABLE refresh_token_sessions ADD COLUMN browser_name VARCHAR(100);
            RAISE NOTICE '✅ Added browser_name to refresh_token_sessions';
        END IF;

        -- Add os_name if missing
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'os_name'
        ) THEN
            ALTER TABLE refresh_token_sessions ADD COLUMN os_name VARCHAR(100);
            RAISE NOTICE '✅ Added os_name to refresh_token_sessions';
        END IF;

        -- Add is_active if missing
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'is_active'
        ) THEN
            ALTER TABLE refresh_token_sessions ADD COLUMN is_active BOOLEAN DEFAULT true;
            RAISE NOTICE '✅ Added is_active to refresh_token_sessions';
        END IF;

        -- Add expires_at if missing (should exist but just in case)
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'expires_at'
        ) THEN
            ALTER TABLE refresh_token_sessions ADD COLUMN expires_at TIMESTAMP;
            RAISE NOTICE '✅ Added expires_at to refresh_token_sessions';
        END IF;

        -- Add revoked_at if missing
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'revoked_at'
        ) THEN
            ALTER TABLE refresh_token_sessions ADD COLUMN revoked_at TIMESTAMP;
            RAISE NOTICE '✅ Added revoked_at to refresh_token_sessions';
        END IF;

        -- Add refresh_count if missing
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_name = 'refresh_token_sessions'
            AND column_name = 'refresh_count'
        ) THEN
            ALTER TABLE refresh_token_sessions ADD COLUMN refresh_count INTEGER DEFAULT 0;
            RAISE NOTICE '✅ Added refresh_count to refresh_token_sessions';
        END IF;

        RAISE NOTICE '✅ All refresh_token_sessions columns verified';
    ELSE
        RAISE NOTICE '⚠️ refresh_token_sessions table does not exist - creating it...';

        -- Create the table if it doesn't exist
        CREATE TABLE refresh_token_sessions (
            id BIGSERIAL PRIMARY KEY,
            user_id BIGINT NOT NULL,
            token VARCHAR(255) NOT NULL UNIQUE,
            refresh_token VARCHAR(255) NOT NULL UNIQUE,
            expires_at TIMESTAMP NOT NULL,
            revoked BOOLEAN DEFAULT false,
            revoked_at TIMESTAMP,
            last_used_at TIMESTAMP,
            user_agent VARCHAR(255),
            ip_address VARCHAR(45),
            device_name VARCHAR(100),
            browser_name VARCHAR(100),
            os_name VARCHAR(100),
            is_active BOOLEAN DEFAULT true,
            refresh_count INTEGER DEFAULT 0,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );

        RAISE NOTICE '✅ Created refresh_token_sessions table with all columns';
    END IF;
END $$;

-- Create indexes for better performance
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'refresh_token_sessions') THEN
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_user_id ON refresh_token_sessions(user_id);
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_token ON refresh_token_sessions(token);
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_refresh_token ON refresh_token_sessions(refresh_token);
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_expires_at ON refresh_token_sessions(expires_at);
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_revoked ON refresh_token_sessions(revoked) WHERE revoked = false;
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_last_used_at ON refresh_token_sessions(last_used_at);
        CREATE INDEX IF NOT EXISTS idx_refresh_token_sessions_is_active ON refresh_token_sessions(is_active) WHERE is_active = true;
        RAISE NOTICE '✅ Created indexes for refresh_token_sessions';
    END IF;
END $$;

-- Add table comment
COMMENT ON TABLE refresh_token_sessions IS 'Refresh token sessions for JWT authentication';
COMMENT ON COLUMN refresh_token_sessions.last_used_at IS 'Timestamp when the token was last used';
COMMENT ON COLUMN refresh_token_sessions.user_agent IS 'User agent of the client';
COMMENT ON COLUMN refresh_token_sessions.ip_address IS 'IP address of the client';
COMMENT ON COLUMN refresh_token_sessions.device_name IS 'Name of the device used';
COMMENT ON COLUMN refresh_token_sessions.browser_name IS 'Browser name used';
COMMENT ON COLUMN refresh_token_sessions.os_name IS 'Operating system name';
COMMENT ON COLUMN refresh_token_sessions.is_active IS 'Whether this session is active';
COMMENT ON COLUMN refresh_token_sessions.refresh_count IS 'Number of times the token has been refreshed';

DO $$
BEGIN
    RAISE NOTICE '✅ Migration V38 completed successfully!';
END $$;