package pl.czyzlowie.module.imgw.entity.synop;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "imgw_synop_station")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImgwSynopStation {

    @Id
    @Column(name = "station_id", length = 10)
    private String stationId;

    @Column(nullable = false)
    private String stationName;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImgwSynopData> measurements = new ArrayList<>();

}
