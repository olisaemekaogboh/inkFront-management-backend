ALTER TABLE contact_messages
ADD COLUMN IF NOT EXISTS preferred_language VARCHAR(10) NOT NULL DEFAULT 'EN';

ALTER TABLE contact_messages
ADD COLUMN IF NOT EXISTS priority VARCHAR(30) NOT NULL DEFAULT 'NORMAL';

ALTER TABLE contact_messages
ADD COLUMN IF NOT EXISTS assigned_to VARCHAR(150);

ALTER TABLE contact_messages
ADD COLUMN IF NOT EXISTS source VARCHAR(80) DEFAULT 'WEBSITE';

ALTER TABLE contact_messages
ADD COLUMN IF NOT EXISTS last_contacted_at TIMESTAMP;

ALTER TABLE contact_messages
ADD COLUMN IF NOT EXISTS resolved_at TIMESTAMP;

UPDATE contact_messages
SET preferred_language = 'EN'
WHERE preferred_language IS NULL;

UPDATE contact_messages
SET priority = 'NORMAL'
WHERE priority IS NULL;

UPDATE contact_messages
SET source = 'WEBSITE'
WHERE source IS NULL;

CREATE INDEX IF NOT EXISTS idx_contact_messages_priority
ON contact_messages(priority);

CREATE INDEX IF NOT EXISTS idx_contact_messages_status
ON contact_messages(status);

CREATE INDEX IF NOT EXISTS idx_contact_messages_created_at
ON contact_messages(created_at);