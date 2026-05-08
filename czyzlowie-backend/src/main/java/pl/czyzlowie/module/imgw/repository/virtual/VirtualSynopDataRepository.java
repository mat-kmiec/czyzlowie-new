package pl.czyzlowie.module.imgw.repository.virtual;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czyzlowie.module.imgw.entity.virtual.VirtualSynopData;
import pl.czyzlowie.module.imgw.entity.virtual.VirtualSynopDataId;

public interface VirtualSynopDataRepository extends JpaRepository<VirtualSynopData, VirtualSynopDataId> {
}
