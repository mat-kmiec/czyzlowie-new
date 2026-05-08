-- liquibase formatted sql

-- changeset author:create_virtual_synop_station
CREATE TABLE virtual_synop_station
(
    station_id   VARCHAR(10)  NOT NULL,
    station_name VARCHAR(255) NOT NULL,
    longitude    DOUBLE PRECISION,
    latitude     DOUBLE PRECISION,
    is_active    BOOLEAN DEFAULT TRUE,
    CONSTRAINT pk_virtual_synop_station PRIMARY KEY (station_id)
);

-- changeset author:create_virtual_synop_data
CREATE TABLE virtual_synop_data
(
    station_id          VARCHAR(10) NOT NULL,
    date_of_measurement DATE        NOT NULL,
    hour_of_measurement INTEGER     NOT NULL,
    temperature         DOUBLE PRECISION,
    wind_speed          DOUBLE PRECISION,
    wind_direction      INTEGER,
    humidity            DOUBLE PRECISION,
    total_precipitation DOUBLE PRECISION,
    pressure            DOUBLE PRECISION,
    CONSTRAINT pk_virtual_synop_data PRIMARY KEY (station_id, date_of_measurement, hour_of_measurement),
    CONSTRAINT fk_virtual_synop_data_on_station FOREIGN KEY (station_id)
        REFERENCES virtual_synop_station (station_id) ON DELETE CASCADE
);

-- changeset author:create_indices_virtual_synop
CREATE INDEX idx_virtual_data_date_hour ON virtual_synop_data (date_of_measurement, hour_of_measurement);