package pl.czyzlowie.module.imgw.entity.warnings;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "imgw_hydro_warning_area")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImgwHydroWarningArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warning_id")
    private ImgwHydroWarning warning;

    private String province;

    @Column(length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "imgw_hydro_warning_catchment", joinColumns = @JoinColumn(name = "area_id"))
    @Column(name = "catchment_code")
    private List<String> catchmentCodes;
}
