package pl.czyzlowie.module.imgw.entity.virtual;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VirtualSynopDataId implements Serializable {
    private String station;
    private LocalDate dateOfMeasurement;
    private Integer hourOfMeasurement;
}
