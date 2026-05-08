package pl.czyzlowie.module.imgw.repository.warnings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.czyzlowie.module.imgw.entity.warnings.ImgwHydroWarning;

import java.time.LocalDateTime;
import java.util.List;

public interface ImgwHydroWarningRepository extends JpaRepository<ImgwHydroWarning, Long> {
    @Query("SELECT DISTINCT w FROM ImgwHydroWarning w JOIN FETCH w.areas WHERE w.validTo > :now")
    List<ImgwHydroWarning> findAllActiveWithAreas(@Param("now") LocalDateTime now);
}
