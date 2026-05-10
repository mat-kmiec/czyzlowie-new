package pl.czyzlowie.module.imgw.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgwMeteoDto {

    @JsonProperty("kod_stacji")
    private String stationId;

    @JsonProperty("nazwa_stacji")
    private String stationName;

    private Double lon;
    private Double lat;

    @JsonProperty("temperatura_gruntu")
    private Double groundTemperature;

    @JsonProperty("temperatura_gruntu_data")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime groundTemperatureDate;

    @JsonProperty("temperatura_powietrza")
    private Double airTemperature;

    @JsonProperty("temperatura_powietrza_data")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime airTemperatureDate;

    @JsonProperty("wiatr_kierunek")
    private Integer windDirection;

    @JsonProperty("wiatr_kierunek_data")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime windDirectionDate;

    @JsonProperty("wiatr_srednia_predkosc")
    private Double windAvgSpeed;

    @JsonProperty("wiatr_srednia_predkosc_data")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime windAvgSpeedDate;

    @JsonProperty("wiatr_predkosc_maksymalna")
    private Double windMaxSpeed;

    @JsonProperty("wiatr_predkosc_maksymalna_data")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime windMaxSpeedDate;

    @JsonProperty("wilgotnosc_wzgledna")
    private Double relativeHumidity;

    @JsonProperty("wilgotnosc_wzgledna_data")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime relativeHumidityDate;

    @JsonProperty("wiatr_poryw_10min")
    private Double windGust10min;

    @JsonProperty("wiatr_poryw_10min_data")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime windGust10minDate;

    @JsonProperty("opad_10min")
    private Double precipitation10min;

    @JsonProperty("opad_10min_data")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime precipitation10minDate;
}