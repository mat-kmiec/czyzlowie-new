-- liquibase formatted sql

-- changeset create_imgw_meteo_station
CREATE TABLE imgw_meteo_station (
    station_id VARCHAR(15) NOT NULL,
    station_name VARCHAR(255) NOT NULL,
    longitude DOUBLE PRECISION,
    latitude DOUBLE PRECISION,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT pk_imgw_meteo_station PRIMARY KEY (station_id)
);

-- changeset create_imgw_meteo_data
CREATE TABLE imgw_meteo_data
(
    station_id               VARCHAR(15) NOT NULL,
    measurement_date_time         TIMESTAMP   NOT NULL,
    ground_temperature       DOUBLE PRECISION,
    ground_temperature_date TIMESTAMP,
    air_temperature          DOUBLE PRECISION,
    air_temperature_date     TIMESTAMP,
    wind_direction           INTEGER,
    wind_direction_date      TIMESTAMP,
    wind_average_speed       DOUBLE PRECISION,
    wind_average_speed_date  TIMESTAMP,
    wind_max_speed           DOUBLE PRECISION,
    wind_max_speed_date      TIMESTAMP,
    relative_humidity        DOUBLE PRECISION,
    relative_humidity_date   TIMESTAMP,
    wind_gust_10min          DOUBLE PRECISION,
    wind_gust_10min_date     TIMESTAMP,
    precipitation_10min      DOUBLE PRECISION,
    precipitation_10min_date TIMESTAMP,
    CONSTRAINT pk_imgw_meteo_data PRIMARY KEY (station_id, measurement_date_time),
    CONSTRAINT fk_imgw_meteo_data_on_station FOREIGN KEY (station_id)
        REFERENCES imgw_meteo_station (station_id) ON DELETE CASCADE
);

-- changeset create index for imgw_meteo_data
CREATE INDEX idx_meteo_data_measurement_time ON imgw_meteo_data (measurement_date_time);
CREATE INDEX idx_meteo_data_air_temperature ON imgw_meteo_data (air_temperature);