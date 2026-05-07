package pl.czyzlowie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CzyzlowieApplication {

    public static void main(String[] args) {
        SpringApplication.run(CzyzlowieApplication.class, args);
    }

}
