package pl.czyzlowie.module.imgw.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgwSynopDto {
    @JsonProperty("id_stacji")
    private String stationId;

    @JsonProperty("stacja")
    private String stationName;

    @JsonProperty("data_pomiaru")
    private String measurementDate;

    @JsonProperty("godzina_pomiaru")
    private Integer measurementHour;

    @JsonProperty("temperatura")
    private Double temperature;

    @JsonProperty("predkosc_wiatru")
    private Double windSpeed;

    @JsonProperty("kierunek_wiatru")
    private Double windDirection;

    @JsonProperty("wilgotnosc_wzgledna")
    private Double humidity;

    @JsonProperty("suma_opadu")
    private Double totalPrecipitation;

    @JsonProperty("cisnienie")
    private Double pressure;

}
