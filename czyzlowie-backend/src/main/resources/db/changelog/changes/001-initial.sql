-- liquibase formatted sql

-- changeset auth:2
CREATE TABLE _user
(
    id        BIGSERIAL PRIMARY KEY,
    firstname VARCHAR(255),
    lastname  VARCHAR(255),
    email     VARCHAR(255) UNIQUE NOT NULL,
    password  VARCHAR(255)        NOT NULL,
    role      VARCHAR(50)         NOT NULL
);

-- changeset auth:3
CREATE TABLE refresh_token
(
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(255) UNIQUE      NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id     BIGINT UNIQUE,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES _user (id) ON DELETE CASCADE
);

-- changeset auth:4
ALTER TABLE _user
    ADD COLUMN is_verified BOOLEAN DEFAULT FALSE NOT NULL;

-- changeset auth:5
CREATE TABLE verification_token
(
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(255) UNIQUE      NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id     BIGINT UNIQUE,
    CONSTRAINT fk_user_verification FOREIGN KEY (user_id) REFERENCES _user (id) ON DELETE CASCADE
);

-- changeset auth:6
ALTER TABLE _user DROP COLUMN firstname;
ALTER TABLE _user DROP COLUMN lastname;

-- changeset auth:7
ALTER TABLE _user
    ADD COLUMN nickname VARCHAR(50) NOT NULL DEFAULT 'user';

-- changeset auth:8
CREATE INDEX idx_user_email ON _user (email);
CREATE INDEX idx_refresh_token_token ON refresh_token (token);
CREATE INDEX idx_refresh_token_user_id ON refresh_token (user_id);

-- changeset auth:9
ALTER TABLE _user
    ALTER COLUMN nickname DROP DEFAULT;
ALTER TABLE _user ALTER COLUMN email TYPE VARCHAR(100);
ALTER TABLE _user ALTER COLUMN role TYPE VARCHAR(20);

-- changeset auth:10
ALTER TABLE refresh_token DROP CONSTRAINT IF EXISTS fk_user_id;
ALTER TABLE refresh_token DROP CONSTRAINT IF EXISTS refresh_token_user_id_key;
ALTER TABLE refresh_token
    ADD CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES _user (id) ON DELETE CASCADE;

-- changeset auth:11
CREATE TABLE password_reset_token
(
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(6)               NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id     BIGINT                   NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_password_reset_user FOREIGN KEY (user_id) REFERENCES _user (id) ON DELETE CASCADE
);

-- changeset auth:12
CREATE INDEX idx_password_reset_token_token ON password_reset_token (token);
CREATE INDEX idx_password_reset_token_user_id ON password_reset_token (user_id);
CREATE INDEX idx_password_reset_token_expiry ON password_reset_token (expiry_date);