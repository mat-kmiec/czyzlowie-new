package pl.czyzlowie.module.imgw.entity.meteo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "imgw_meteo_station")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImgwMeteoStation {

    @Id
    @Column(name = "station_id", length = 15)
    private String stationId;

    @Column(nullable = false)
    private String stationName;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "is_active")
    private Boolean isActive;


}

