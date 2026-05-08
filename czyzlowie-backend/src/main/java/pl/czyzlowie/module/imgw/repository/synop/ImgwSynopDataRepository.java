package pl.czyzlowie.module.imgw.repository.synop;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czyzlowie.module.imgw.entity.synop.ImgwSynopData;
import pl.czyzlowie.module.imgw.entity.synop.ImgwSynopDataId;


public interface ImgwSynopDataRepository extends JpaRepository<ImgwSynopData, ImgwSynopDataId> {
}
