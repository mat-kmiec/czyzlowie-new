-- liquibase formatted sql

-- changeset setup:1
CREATE TABLE test_table (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- changeset auth:2
CREATE TABLE _user (
    id BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- changeset auth:3
CREATE TABLE refresh_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id BIGINT UNIQUE,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES _user (id) ON DELETE CASCADE
);

-- changeset auth:4
ALTER TABLE _user ADD COLUMN is_verified BOOLEAN DEFAULT FALSE NOT NULL;

-- changeset auth:5
CREATE TABLE verification_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id BIGINT UNIQUE,
    CONSTRAINT fk_user_verification FOREIGN KEY (user_id) REFERENCES _user (id) ON DELETE CASCADE
);

-- changeset auth:6
ALTER TABLE _user DROP COLUMN firstname;
ALTER TABLE _user DROP COLUMN lastname;
