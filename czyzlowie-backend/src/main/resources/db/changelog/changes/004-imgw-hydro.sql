-- liquibase formatted sql

--changeset create_imgw_hydro_station
CREATE TABLE imgw_hydro_station (
    station_id VARCHAR(15) NOT NULL,
    station_name VARCHAR(255) NOT NULL,
    river_name VARCHAR(255),
    province VARCHAR(255),
    longitude DOUBLE PRECISION,
    latitude DOUBLE PRECISION,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT pk_imgw_hydro_station PRIMARY KEY (station_id)
);

-- changeset create_imgw_hydro_data
CREATE TABLE imgw_hydro_data (
    station_id VARCHAR(15) NOT NULL,
    measurement_date_time    TIMESTAMP   NOT NULL,
    water_level              INTEGER,
    water_level_date         TIMESTAMP,
    water_temperature        DOUBLE PRECISION,
    water_temperature_date   TIMESTAMP,
    water_flow               DOUBLE PRECISION,
    water_flow_date          TIMESTAMP,
    ice_phenomenon           INTEGER,
    ice_phenomenon_date      TIMESTAMP,
    overgrow_phenomenon      INTEGER,
    overgrow_phenomenon_date TIMESTAMP,
    CONSTRAINT pk_imgw_hydro_data PRIMARY KEY (station_id, measurement_date_time),
    CONSTRAINT fk_imgw_hydro_data_on_station FOREIGN KEY (station_id)
        REFERENCES imgw_hydro_station (station_id) ON DELETE CASCADE
);

-- changeset create index for imgw-hydro-data
CREATE INDEX idx_hydro_data_measurement_time ON imgw_hydro_data (measurement_date_time);
CREATE INDEX idx_hydro_data_water_level ON imgw_hydro_data (water_level);