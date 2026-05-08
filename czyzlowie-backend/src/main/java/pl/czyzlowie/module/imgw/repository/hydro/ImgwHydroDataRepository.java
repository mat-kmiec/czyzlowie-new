package pl.czyzlowie.module.imgw.repository.hydro;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czyzlowie.module.imgw.entity.hydro.ImgwHydroData;
import pl.czyzlowie.module.imgw.entity.hydro.ImgwHydroDataId;

import java.util.List;

public interface ImgwHydroDataRepository extends JpaRepository<ImgwHydroData, ImgwHydroDataId> {
}
