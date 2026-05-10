package pl.czyzlowie.module.imgw.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "imgw.api")
@Getter
@Setter
public class ImgwApiProperties {
    private ApiEndpoint synop;
    private ApiEndpoint meteo;
    private ApiEndpoint hydro;

    @Getter
    @Setter
    public static class ApiEndpoint{
        private String url;
        private String cron;
    }
}
