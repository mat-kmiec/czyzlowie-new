package pl.czyzlowie.module.imgw.entity.warnings;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "imgw_meteo_warning")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImgwMeteoWarning {

    @Id
    @Column(name = "warning_id")
    private String warningId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "level")
    private Integer level;

    @Column(name = "probability")
    private Integer probability;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "content", length = 2000)
    private String content;

    @Column(name = "comment", length = 1000)
    private String comment;

    @Column(name = "office")
    private String office;

    @ElementCollection
    @CollectionTable(name = "imgw_meteo_warning_teryt", joinColumns = @JoinColumn(name = "warning_id"))
    @Column(name = "teryt_code")
    private Set<String> terytCodes;
}
