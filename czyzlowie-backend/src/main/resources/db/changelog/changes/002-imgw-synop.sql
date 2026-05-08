-- liquibase formatted sql

-- changeset: create_imgw_synop_station
CREATE TABLE imgw_synop_station (
    station_id VARCHAR(10) NOT NULL,
    station_name VARCHAR(255) NOT NULL,
    longitude DOUBLE PRECISION,
    latitude DOUBLE PRECISION,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT pk_imgw_synop_station PRIMARY KEY (station_id)
);

-- changeset: create_imgw_data
CREATE TABLE imgw_synop_data (
    station_id VARCHAR(10) NOT NULL,
    measurement_time TIMESTAMP NOT NULL,
    temperature DOUBLE PRECISION,
    wind_speed DOUBLE PRECISION,
    wind_direction INTEGER,
    humidity DOUBLE PRECISION,
    total_precipitation DOUBLE PRECISION,
    pressure DOUBLE PRECISION,
    CONSTRAINT pk_imgw_synop_data PRIMARY KEY (station_id, measurement_time),
    CONSTRAINT fk_imgw_synop_data_station FOREIGN KEY (station_id) REFERENCES imgw_synop_station (station_id) ON DELETE CASCADE
);

-- changeset: create index for imgw_synop_data
CREATE INDEX idx_imgw_synop_data_measurement_time ON imgw_synop_data(measurement_time);
CREATE INDEX idx_imgw_synop_data_temperature ON imgw_synop_data(temperature);
CREATE INDEX idx_imgw_synop_data_pressure ON imgw_synop_data(pressure);
