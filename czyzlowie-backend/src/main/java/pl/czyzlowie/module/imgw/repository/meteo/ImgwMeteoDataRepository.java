package pl.czyzlowie.module.imgw.repository.meteo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czyzlowie.module.imgw.entity.meteo.ImgwMeteoData;
import pl.czyzlowie.module.imgw.entity.meteo.ImgwMeteoDataId;

import java.util.Optional;

public interface ImgwMeteoDataRepository extends JpaRepository<ImgwMeteoData, ImgwMeteoDataId> {
}
