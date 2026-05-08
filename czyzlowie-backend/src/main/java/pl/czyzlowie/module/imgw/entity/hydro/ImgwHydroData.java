package pl.czyzlowie.module.imgw.entity.hydro;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "imgw_hydro_data")
@IdClass(ImgwHydroDataId.class)
@Getter
@Setter
@NoArgsConstructor
public class ImgwHydroData {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private ImgwHydroStation station;

    @Id
    @Column(name = "measurement_date_time")
    private LocalDateTime measurementDateTime;

    @Column(name = "water_level")
    private Integer waterLevel;

    @Column(name = "water_level_date")
    private LocalDateTime waterLevelDate;

    @Column(name = "water_temperature")
    private Double waterTemperature;

    @Column(name = "water_temperature_date")
    private LocalDateTime waterTemperatureDate;

    @Column(name = "water_flow")
    private Double waterFlow;

    @Column(name = "water_flow_date")
    private LocalDateTime waterFlowDate;

    @Column(name = "ice_phenomenon")
    private Integer icePhenomenon;

    @Column(name = "ice_phenomenon_date")
    private LocalDateTime icePhenomenonDate;

    @Column(name = "overgrow_phenomenon")
    private Integer overgrowPhenomenon;

    @Column(name = "overgrow_phenomenon_date")
    private LocalDateTime overgrowPhenomenonDate;



}
