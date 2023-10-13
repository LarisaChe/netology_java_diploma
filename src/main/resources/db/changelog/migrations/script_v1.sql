-- changeset lache:2.1
ALTER TABLE cloud.users ALTER COLUMN password TYPE VARCHAR(255);
-- rollback

-- changeset lache:2.2
ALTER TABLE cloud.users ADD COLUMN salt VARCHAR(1024);
-- rollback
