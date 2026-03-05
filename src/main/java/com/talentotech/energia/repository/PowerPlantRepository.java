package com.talentotech.energia.repository;
import com.talentotech.energia.model.PowerPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
public interface PowerPlantRepository extends JpaRepository<PowerPlant, Long> {

    // Buscar plantas por empresa
    List<PowerPlant> findByCompanyId(Long companyId);

    // Buscar plantas por región
    List<PowerPlant> findByRegionId(Long regionId);

    // Validar planta única por empresa
    Optional<PowerPlant> findByNameAndCompanyId(String name, Long companyId);

    boolean existsByNameAndCompanyId(String name, Long companyId);
}
