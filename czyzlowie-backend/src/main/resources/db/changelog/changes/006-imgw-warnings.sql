-- liquibase formatted sql

-- changeset system:imgw-warnings-001
CREATE TABLE imgw_meteo_warning
(
    warning_id   VARCHAR(50)  NOT NULL,
    event_name   VARCHAR(255) NOT NULL,
    level        INTEGER,
    probability  INTEGER,
    valid_from   TIMESTAMP,
    valid_to     TIMESTAMP,
    published_at TIMESTAMP,
    content      TEXT,
    comment      TEXT,
    office       VARCHAR(255),
    CONSTRAINT pk_imgw_meteo_warning PRIMARY KEY (warning_id)
);

CREATE TABLE imgw_meteo_warning_teryt
(
    warning_id VARCHAR(50) NOT NULL,
    teryt_code VARCHAR(10) NOT NULL,
    CONSTRAINT fk_meteo_teryt_warning FOREIGN KEY (warning_id)
        REFERENCES imgw_meteo_warning (warning_id) ON DELETE CASCADE
);

-- changeset author:create_warnings_hydro
CREATE TABLE imgw_hydro_warning
(
    id             BIGSERIAL NOT NULL,
    warning_number INTEGER,
    published_at   TIMESTAMP NOT NULL,
    level          INTEGER,
    valid_from     TIMESTAMP,
    valid_to       TIMESTAMP,
    probability    INTEGER,
    event_name     VARCHAR(255),
    course         TEXT,
    comment        TEXT,
    office         VARCHAR(255),
    CONSTRAINT pk_imgw_hydro_warning PRIMARY KEY (id)
);

CREATE TABLE imgw_hydro_warning_area
(
    id          BIGSERIAL NOT NULL,
    warning_id  BIGINT    NOT NULL,
    province    VARCHAR(100),
    description TEXT,
    CONSTRAINT pk_imgw_hydro_warning_area PRIMARY KEY (id),
    CONSTRAINT fk_hydro_area_warning FOREIGN KEY (warning_id) REFERENCES imgw_hydro_warning (id) ON DELETE CASCADE
);

CREATE TABLE imgw_hydro_warning_catchment
(
    area_id        BIGINT      NOT NULL,
    catchment_code VARCHAR(50) NOT NULL,
    CONSTRAINT fk_hydro_catchment_area FOREIGN KEY (area_id) REFERENCES imgw_hydro_warning_area (id) ON DELETE CASCADE
);

-- changeset author:warning_indices
CREATE INDEX idx_meteo_warning_valid_to ON imgw_meteo_warning (valid_to);
CREATE INDEX idx_meteo_teryt_code ON imgw_meteo_warning_teryt (teryt_code);
CREATE INDEX idx_hydro_warning_valid_to ON imgw_hydro_warning (valid_to);