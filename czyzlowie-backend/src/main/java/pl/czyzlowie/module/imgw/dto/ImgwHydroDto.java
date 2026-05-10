package pl.czyzlowie.module.imgw.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgwHydroDto {

    @JsonProperty("id_stacji")
    private String stationId;

    @JsonProperty("stacja")
    private String stationName;

    @JsonProperty("rzeka")
    private String riverName;

    @JsonProperty("wojewodztwo")
    private String province;

    private Double lon;
    private Double lat;

    @JsonProperty("stan_wody")
    private Integer waterLevel;

    @JsonProperty("stan_wody_data_pomiaru")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime waterLevelDate;

    @JsonProperty("temperatura_wody")
    private Double waterTemperature;

    @JsonProperty("temperatura_wody_data_pomiaru")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime waterTemperatureDate;

    @JsonProperty("przeplyw")
    private Double waterFlow;

    @JsonProperty("przeplyw_data")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime waterFlowDate;

    @JsonProperty("zjawisko_lodowe")
    private Integer icePhenomenon;

    @JsonProperty("zjawisko_lodowe_data_pomiaru")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime icePhenomenonDate;

    @JsonProperty("zjawisko_zarastania")
    private Integer overgrowPhenomenon;

    @JsonProperty("zjawisko_zarastania_data_pomiaru")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime overgrowPhenomenonDate;
}
