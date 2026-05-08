package pl.czyzlowie.module.imgw.entity.synop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "imgw_synop_data")
@IdClass(ImgwSynopDataId.class)
@Getter
@Setter
@NoArgsConstructor
public class ImgwSynopData {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private ImgwSynopStation station;

    @Id
    @Column(name = "measurement_time")
    private LocalDateTime measurementTime;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "wind_speed")
    private Double windSpeed;

    @Column(name = "wind_direction")
    private Integer windDirection;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "total_precipitation")
    private Double totalPrecipitation;

    @Column(name = "pressure")
    private Double pressure;

}
