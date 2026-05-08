package pl.czyzlowie.module.imgw.repository.synop;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czyzlowie.module.imgw.entity.synop.ImgwSynopStation;

public interface ImgwSynopStationRepository extends JpaRepository<ImgwSynopStation, String> {}
