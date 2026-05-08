package pl.czyzlowie.module.imgw.entity.synop;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ImgwSynopDataId implements Serializable {
    private String station;
    private LocalDateTime measurementTime;
}
