package com.talentotech.energia.repository;

import com.talentotech.energia.model.PowerPlant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerPlantRepository extends JpaRepository<PowerPlant, Long> {

    // Buscar plantas por empresa
    List<PowerPlant> findByCompanyId(Long companyId);

    // Buscar plantas por región
    List<PowerPlant> findByRegionId(Long regionId);

    // Validar planta única por empresa
    Optional<PowerPlant> findByNameAndCompanyId(String name, Long companyId);

    boolean existsByNameAndCompanyId(String name, Long companyId);
}
