package pl.czyzlowie.module.imgw.repository.virtual;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.czyzlowie.module.imgw.entity.virtual.VirtualSynopStation;

public interface VirtualSynopStationRepository extends JpaRepository<VirtualSynopStation, String> {}
