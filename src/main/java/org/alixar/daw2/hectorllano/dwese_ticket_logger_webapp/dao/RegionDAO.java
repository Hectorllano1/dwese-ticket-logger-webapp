package org.alixar.daw2.hectorllano.dwese_ticket_logger_webapp.dao;

import org.alixar.daw2.hectorllano.dwese_ticket_logger_webapp.entities.Region;

import java.util.List;

public interface RegionDAO {
    List<Region> listAllRegions();

    void insertRegion(Region region);

    void updateRegion(Region region);

    void deleteRegion(Long id);

    Region getRegionById(Long id);

    boolean existsRegionByCode(String code);

    boolean existsRegionByCodeAndNotId(String code, Long id);
}
