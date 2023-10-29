-- changeset lache:1.0
CREATE SCHEMA IF NOT EXISTS cloud AUTHORIZATION postgres;

-- changeset lache:1.1
CREATE TABLE cloud.users (
	login VARCHAR(32) PRIMARY KEY,
	password VARCHAR(255) NOT NULL,
    role VARCHAR(32)
);
-- rollback

-- changeset lache:1.2
insert into cloud.users (login, password, role) VALUES ('test@test.ru','$2a$10$sr06xGB3bJnPv4e./nkL6.EHKEVanESe7VDA0mZKoWPY1PWQzGCUi','USER');
-- rollback
