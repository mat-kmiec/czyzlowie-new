package pl.czyzlowie.module.imgw.entity.meteo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "imgw_meteo_data")
@IdClass(ImgwMeteoDataId.class)
@Getter
@Setter
@NoArgsConstructor
public class ImgwMeteoData {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private ImgwMeteoStation station;

    @Id
    @Column(name = "measurement_date_time")
    private LocalDateTime measurementDateTime;

    @Column(name = "ground_temperature")
    private Double groundTemperature;

    @Column(name = "ground_temperature_date")
    private LocalDateTime groundTemperatureDate;

    @Column(name = "air_temperature")
    private Double airTemperature;

    @Column(name = "air_temperature_date")
    private LocalDateTime airTemperatureDate;

    @Column(name = "wind_direction")
    private Integer windDirection;

    @Column(name = "wind_direction_date")
    private LocalDateTime windDirectionDate;

    @Column(name = "wind_average_speed")
    private Double windAverageSpeed;

    @Column(name = "wind_average_speed_date")
    private LocalDateTime windAverageSpeedDate;

    @Column(name = "wind_max_speed")
    private Double windMaxSpeed;

    @Column(name = "wind_max_speed_date")
    private LocalDateTime windMaxSpeedDate;

    @Column(name = "relative_humidity")
    private Double relativeHumidity;

    @Column(name = "relative_humidity_date")
    private LocalDateTime relativeHumidityDate;

    @Column(name = "wind_gust_10min")
    private Double windGust10min;

    @Column(name = "wind_gust_10min_date")
    private LocalDateTime windGust10minDate;

    @Column(name = "precipitation_10min")
    private Double precipitation10min;

    @Column(name = "precipitation_10min_date")
    private LocalDateTime precipitation10minDate;
}
