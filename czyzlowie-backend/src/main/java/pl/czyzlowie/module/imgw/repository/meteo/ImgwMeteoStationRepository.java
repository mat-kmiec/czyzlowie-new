package pl.czyzlowie.module.imgw.repository.meteo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czyzlowie.module.imgw.entity.meteo.ImgwMeteoStation;

public interface ImgwMeteoStationRepository extends JpaRepository<ImgwMeteoStation, String> {}
