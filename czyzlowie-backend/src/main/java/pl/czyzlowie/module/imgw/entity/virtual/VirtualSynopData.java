package pl.czyzlowie.module.imgw.entity.virtual;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "virtual_synop_data")
@IdClass(VirtualSynopDataId.class)
@Getter
@Setter
@NoArgsConstructor
public class VirtualSynopData {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private VirtualSynopStation station;

    @Id
    @Column(name = "date_of_measurement")
    private LocalDate dateOfMeasurement;

    @Id
    @Column(name = "hour_of_measurement")
    private Integer hourOfMeasurement;

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
