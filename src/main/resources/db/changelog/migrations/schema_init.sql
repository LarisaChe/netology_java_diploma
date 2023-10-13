-- changeset lache:1.0
CREATE SCHEMA cloud;

-- changeset lache:1.1
CREATE TABLE cloud.users (
	login VARCHAR(32) PRIMARY KEY,
	password VARCHAR(100) NOT NULL
);

-- changeset lache:1.2
CREATE TABLE cloud.files(
	id SERIAL PRIMARY KEY,
	dt timestamp with time zone DEFAULT now(),
	login VARCHAR(32) NOT NULL REFERENCES cloud.users (login),
	file_name VARCHAR(1000) NOT NULL,
	size bigint,
	file_status VARCHAR(10) NOT NULL DEFAULT 'ADDED' 
);
-- rollback
