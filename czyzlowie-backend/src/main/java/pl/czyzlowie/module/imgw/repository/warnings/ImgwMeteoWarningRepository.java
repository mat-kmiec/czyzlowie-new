package pl.czyzlowie.module.imgw.repository.warnings;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czyzlowie.module.imgw.entity.warnings.ImgwMeteoWarning;

public interface ImgwMeteoWarningRepository extends JpaRepository<ImgwMeteoWarning, String> {

}
