package pl.czyzlowie.module.imgw.entity.warnings;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "imgw_hydro_warning")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImgwHydroWarning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "warning_number")
    private Integer warningNumber;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "level")
    private Integer level;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Column(name = "probability")
    private Integer probability;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "course", length = 2000)
    private String course;

    @Column(name = "comment", length = 1000)
    private String comment;

    @Column(name = "office")
    private String office;

    @OneToMany(mappedBy = "warning", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImgwHydroWarningArea> areas = new ArrayList<>();
}
