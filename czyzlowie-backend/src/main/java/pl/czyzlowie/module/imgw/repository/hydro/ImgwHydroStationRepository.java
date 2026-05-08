package pl.czyzlowie.module.imgw.repository.hydro;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czyzlowie.module.imgw.entity.hydro.ImgwHydroStation;

public interface ImgwHydroStationRepository extends JpaRepository<ImgwHydroStation, String> {}
