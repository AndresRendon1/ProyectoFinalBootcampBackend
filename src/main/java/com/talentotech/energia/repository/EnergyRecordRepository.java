package com.talentotech.energia.repository;

import com.talentotech.energia.model.EnergyRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnergyRecordRepository extends JpaRepository<EnergyRecord, Long> {

    List<EnergyRecord> findByPowerPlantId(Long powerPlantId);

    List<EnergyRecord> findByMeasurementTypeId(Long measurementTypeId);

    Optional<EnergyRecord> findByPowerPlantIdAndYearAndMonthAndMeasurementTypeId(
            Long powerPlantId, Integer year, Integer month, Long measurementTypeId);

    boolean existsByPowerPlantIdAndYearAndMonthAndMeasurementTypeId(
            Long powerPlantId, Integer year, Integer month, Long measurementTypeId);
}
