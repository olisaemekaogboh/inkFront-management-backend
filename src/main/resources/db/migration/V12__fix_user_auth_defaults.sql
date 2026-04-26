ALTER TABLE users
    ADD COLUMN IF NOT EXISTS email_verified BOOLEAN;

UPDATE users
SET email_verified = FALSE
WHERE email_verified IS NULL;

ALTER TABLE users
    ALTER COLUMN email_verified SET DEFAULT FALSE,
    ALTER COLUMN email_verified SET NOT NULL;


ALTER TABLE users
    ADD COLUMN IF NOT EXISTS provider VARCHAR(30);

UPDATE users
SET provider = 'LOCAL'
WHERE provider IS NULL OR provider = '';

ALTER TABLE users
    ALTER COLUMN provider SET DEFAULT 'LOCAL',
    ALTER COLUMN provider SET NOT NULL;


ALTER TABLE users
    ADD COLUMN IF NOT EXISTS provider_user_id VARCHAR(190);

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS avatar_url VARCHAR(700);

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS display_name VARCHAR(180);

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS username VARCHAR(190);

UPDATE users
SET username = email
WHERE username IS NULL OR username = '';

UPDATE users
SET display_name = email
WHERE display_name IS NULL OR display_name = '';


CREATE INDEX IF NOT EXISTS idx_users_provider_provider_user_id
    ON users (provider, provider_user_id);

CREATE INDEX IF NOT EXISTS idx_users_username
    ON users (username);