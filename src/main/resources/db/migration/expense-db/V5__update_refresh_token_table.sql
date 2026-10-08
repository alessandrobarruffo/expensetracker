ALTER TABLE refresh_token ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';
UPDATE refresh_token SET status = 'REVOKED' WHERE revoked = true;
ALTER TABLE refresh_token DROP COLUMN revoked;